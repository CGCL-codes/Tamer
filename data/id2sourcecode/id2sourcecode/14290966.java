    public static byte[] sha1(byte[] value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(value);
        sha1hash = md.digest();
        return sha1hash;
    }
