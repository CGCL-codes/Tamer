    public static byte[] md5(byte[] input) {
        try {
            java.security.MessageDigest md;
            md = java.security.MessageDigest.getInstance("MD5");
            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;
    }
