    public static byte[] generateKeyID(String digestAlg, Key key) {
        byte[] id = null;
        try {
            byte[] encoding = key.getEncoded();
            id = DigestHelper.digest(digestAlg, encoding);
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new RuntimeException("Error: can't find " + digestAlg + "!  " + ex.toString());
        }
        return id;
    }
