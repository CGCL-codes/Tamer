    public static byte[] md5(byte[] text) {
        IMessageDigest md = HashFactory.getInstance("MD5");
        md.update(text, 0, text.length);
        return md.digest();
    }
