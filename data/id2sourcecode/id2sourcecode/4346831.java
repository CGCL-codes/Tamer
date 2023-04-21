    public static byte[] md5(byte[]... args) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            for (byte[] arg : args) {
                digest.update(arg);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
