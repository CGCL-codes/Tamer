    public static byte[] md5(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash;
        md.update(value);
        md5hash = md.digest();
        return md5hash;
    }
