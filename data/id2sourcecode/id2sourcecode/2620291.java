    public static String md5(byte[] data) {
        md5Digest.reset();
        return toHexString(md5Digest.digest(data));
    }
