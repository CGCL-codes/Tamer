    public static byte[] digest(byte[] content, int offset, int length) {
        CCNDigestHelper dh = new CCNDigestHelper();
        dh.update(content, offset, length);
        return dh.digest();
    }
