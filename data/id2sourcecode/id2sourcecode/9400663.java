    public static String createCnonce() {
        String cnonce;
        MessageDigest md5Helper = createMessageDigest("MD5");
        cnonce = Long.toString(System.currentTimeMillis());
        cnonce = encode(md5Helper.digest(EncodingUtils.getAsciiBytes(cnonce)));
        return cnonce;
    }
