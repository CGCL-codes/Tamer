    public static final String getMD5Hash(byte[] data, int offset, int length) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return generateHash(md5.digest());
    }
