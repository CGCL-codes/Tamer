    public static void toggleLongEndian(byte[] b, int off, int len) {
        if (b == null || len == 0) return;
        int end = off + len;
        if (off < 0 || len < 0 || end > b.length) throw new IndexOutOfBoundsException("b.length = " + b.length + ", off = " + off + ", len = " + len);
        if ((len & 7) != 0) throw new IllegalArgumentException("len = " + len);
        byte tmp;
        for (int i = off; i < end; i += 8) {
            tmp = b[i];
            b[i] = b[i + 7];
            b[i + 7] = tmp;
            tmp = b[i + 1];
            b[i + 1] = b[i + 6];
            b[i + 6] = tmp;
            tmp = b[i + 2];
            b[i + 2] = b[i + 5];
            b[i + 5] = tmp;
            tmp = b[i + 3];
            b[i + 3] = b[i + 4];
            b[i + 4] = tmp;
        }
    }
