    public static byte[] hashData(byte[] bytes) {
        try {
            return MessageDigest.getInstance("MD5").digest(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
