    public static byte[] md5(byte[] input) {
        MessageDigest md5 = MD5.get();
        md5.reset();
        md5.update(input);
        return md5.digest();
    }
