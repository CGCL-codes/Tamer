    public static byte[] computeSHA1(byte[] expression) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(expression);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 not supported on this platform");
        }
        return md.digest();
    }
