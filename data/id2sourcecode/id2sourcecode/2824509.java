    private static String generateCertificateThumbprint(Certificate certificate) {
        try {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] encoded;
            try {
                encoded = certificate.getEncoded();
            } catch (CertificateEncodingException e) {
                throw new RuntimeException(e);
            }
            return HexUtils.toHex(md.digest(encoded), ":");
        } catch (Exception e) {
            return null;
        }
    }
