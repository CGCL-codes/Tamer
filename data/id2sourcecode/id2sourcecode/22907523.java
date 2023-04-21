    private byte[] generateTimestampToken(byte[] toBeTimestamped) throws CertificateException, IOException {
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
            }
        }
        byte[] digest = messageDigest.digest(toBeTimestamped);
        TSRequest tsQuery = new TSRequest(digest, "SHA-1");
        BigInteger nonce = null;
        if (RANDOM != null) {
            nonce = new BigInteger(64, RANDOM);
            tsQuery.setNonce(nonce);
        }
        tsQuery.requestCertificate(tsRequestCertificate);
        Timestamper tsa = new HttpTimestamper(tsaUrl);
        TSResponse tsReply = tsa.generateTimestamp(tsQuery);
        int status = tsReply.getStatusCode();
        if (status != 0 && status != 1) {
            int failureCode = tsReply.getFailureCode();
            if (failureCode == -1) {
                throw new IOException("Error generating timestamp: " + tsReply.getStatusCodeAsText());
            } else {
                throw new IOException("Error generating timestamp: " + tsReply.getStatusCodeAsText() + " " + tsReply.getFailureCodeAsText());
            }
        }
        PKCS7 tsToken = tsReply.getToken();
        TimestampToken tst = new TimestampToken(tsToken.getContentInfo().getData());
        if (!tst.getHashAlgorithm().equals(new AlgorithmId(new ObjectIdentifier("1.3.14.3.2.26")))) {
            throw new IOException("Digest algorithm not SHA-1 in timestamp token");
        }
        if (!Arrays.equals(tst.getHashedMessage(), digest)) {
            throw new IOException("Digest octets changed in timestamp token");
        }
        ;
        BigInteger replyNonce = tst.getNonce();
        if (replyNonce == null && nonce != null) {
            throw new IOException("Nonce missing in timestamp token");
        }
        if (replyNonce != null && !replyNonce.equals(nonce)) {
            throw new IOException("Nonce changed in timestamp token");
        }
        List<String> keyPurposes = null;
        X509Certificate[] certs = tsToken.getCertificates();
        if (certs != null && certs.length > 0) {
            for (X509Certificate cert : certs) {
                boolean isSigner = false;
                for (X509Certificate cert2 : certs) {
                    if (cert != cert2) {
                        if (cert.getSubjectDN().equals(cert2.getIssuerDN())) {
                            isSigner = true;
                            break;
                        }
                    }
                }
                if (!isSigner) {
                    keyPurposes = cert.getExtendedKeyUsage();
                    if (keyPurposes == null || !keyPurposes.contains(KP_TIMESTAMPING_OID)) {
                        throw new CertificateException("Certificate is not valid for timestamping");
                    }
                    break;
                }
            }
        }
        return tsReply.getEncodedToken();
    }
