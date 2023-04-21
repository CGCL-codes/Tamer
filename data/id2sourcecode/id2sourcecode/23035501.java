    public static byte[] digest(byte[] content, int offset, int length) {
        DigestHelper dh = new DigestHelper();
        dh.update(content, offset, length);
        return dh.digest();
    }
