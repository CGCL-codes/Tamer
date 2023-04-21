    public static byte[] generateCertID(String digestAlg, Certificate cert) throws CertificateEncodingException {
        byte[] id = null;
        try {
            byte[] encoding = cert.getEncoded();
            id = DigestHelper.digest(digestAlg, encoding);
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error: can't find " + digestAlg + "!  " + ex.toString());
        }
        return id;
    }
