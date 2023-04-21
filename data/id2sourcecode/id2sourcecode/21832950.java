    public static byte[] md5Digest(byte[] src) {
        byte[] bytes = null;
        try {
            java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");
            bytes = alg.digest(src);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return bytes;
    }
