    public static byte[] hashBytes(Digest d, byte[] b, int offset, int length) {
        d.update(b, offset, length);
        return d.digest();
    }
