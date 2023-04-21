    public static byte[] md5(byte[] b) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(b);
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }
