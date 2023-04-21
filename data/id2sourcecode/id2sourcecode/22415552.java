    private byte[] calculateSignerId(List<? extends X509Certificate> certs) throws SignatureException {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance(X509);
            CertPath path = certFactory.generateCertPath(certs);
            byte[] encodedCertPath = path.getEncoded(PKI_PATH_ENCODING);
            MessageDigest digest = MessageDigest.getInstance(AlgorithmUtil.getJceName(getHashAlgorithm()));
            return digest.digest(encodedCertPath);
        } catch (CertificateException e) {
            throw new SignatureException("could not parse certificate chain", e);
        } catch (NoSuchAlgorithmException e) {
            throw new SignatureException("could not calculate hash of cert chain", e);
        }
    }
