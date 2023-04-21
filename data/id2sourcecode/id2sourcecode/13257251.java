    private static byte[] digest(String type, byte[] bytes) {
        try {
            MessageDigest dist = MessageDigest.getInstance(type);
            return dist.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Cannot find digest:" + type, e);
        }
    }
