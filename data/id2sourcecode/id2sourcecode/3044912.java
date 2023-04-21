    public static byte[] digest(byte[] d, int offset) {
        if (md == null) init();
        if (d == null) return md.digest(zero); else {
            md.update(d, offset, d.length - offset);
            return md.digest();
        }
    }
