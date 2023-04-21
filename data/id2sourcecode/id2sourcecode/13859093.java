    public static byte[] digest(byte[] d, int offset) {
        if (md == null) init();
        md.update(d, offset, d.length - offset);
        return md.digest();
    }
