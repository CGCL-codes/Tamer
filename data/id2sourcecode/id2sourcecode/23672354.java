    public static byte[] calculateSHA1(byte[] data) {
        MessageDigest sha1 = createSHA1();
        sha1.update(data);
        return sha1.digest();
    }
