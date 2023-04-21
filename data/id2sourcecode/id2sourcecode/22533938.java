    public static byte[] sha1(byte[] buffer) {
        if (digestSha1 == null) {
            throw new RuntimeException("MessageDigest not instantiated!");
        }
        return digestSha1.digest(buffer);
    }
