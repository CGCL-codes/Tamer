    public static byte[] md5(byte[] text1, byte[] text2) {
        IMessageDigest md = HashFactory.getInstance("MD5");
        md.update(text1, 0, text1.length);
        md.update(text2, 0, text2.length);
        return md.digest();
    }
