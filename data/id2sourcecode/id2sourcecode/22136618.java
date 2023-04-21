    public static byte[] sha1(byte[] b) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(b);
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }
