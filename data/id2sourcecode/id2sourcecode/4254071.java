    public static void toggleShortEndian(byte[] b, int off, int len) {
        if (b == null || len == 0) return;
        int end = off + len;
        if (off < 0 || len < 0 || end > b.length) throw new IndexOutOfBoundsException("b.length = " + b.length + ", off = " + off + ", len = " + len);
        if ((len & 1) != 0) throw new IllegalArgumentException("len = " + len);
        byte tmp;
        for (int i = off; i < end; i++, i++) {
            tmp = b[i];
            b[i] = b[i + 1];
            b[i + 1] = tmp;
        }
    }
