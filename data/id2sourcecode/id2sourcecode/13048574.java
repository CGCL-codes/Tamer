    private static final void swapL(byte[] b, int s, int len) {
        if ((len) % 4 != 0) {
            return;
        }
        len += s;
        byte bb;
        for (int i = s; i < len; i += 4) {
            bb = b[i];
            b[i] = b[i + 3];
            b[i + 3] = bb;
            bb = b[i + 1];
            b[i + 1] = b[i + 2];
            b[i + 2] = bb;
        }
    }
