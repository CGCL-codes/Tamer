    public static byte[] md5(byte[] buffer) {
        if (digestMd5 == null) {
            throw new RuntimeException("MessageDigest not instantiated!");
        }
        return digestMd5.digest(buffer);
    }
