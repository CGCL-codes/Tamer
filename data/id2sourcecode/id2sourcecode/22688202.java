    public static final byte[] getMD5hash(byte[] b) {
        MD5D.update(b, 0, b.length);
        return MD5D.digest();
    }
