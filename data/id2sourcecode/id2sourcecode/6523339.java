    public static byte[] getMd5Digest(byte[] input) {
        MessageDigest md5 = perThreadMd5.get();
        md5.reset();
        md5.update(input);
        return md5.digest();
    }
