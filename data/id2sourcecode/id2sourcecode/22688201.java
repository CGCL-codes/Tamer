    public static final String getMD5hash(String source) {
        byte[] dataToHash = source.getBytes();
        MD5D.update(dataToHash, 0, dataToHash.length);
        return toHexString(MD5D.digest());
    }
