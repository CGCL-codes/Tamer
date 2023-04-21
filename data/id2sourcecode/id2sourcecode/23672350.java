    public static byte[] calculateMD5(byte[] data) {
        MessageDigest md5 = createMD5();
        md5.update(data);
        return md5.digest();
    }
