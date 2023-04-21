    public static final String MD5hash(byte[] clearText) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(HASH_ALGORITHM);
        md5.reset();
        md5.update(clearText);
        return asHex(md5.digest());
    }
