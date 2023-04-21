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
                    if (!keyPurposes.contains(KP_TIMESTAMPING_OID)) {
                        throw new CertificateException("Certificate is not valid for timestamping");
                    }
                    break;
                }
            }
        }
        return tsReply.getEncodedToken();
    }
