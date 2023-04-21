    private static final void swapS(byte[] b, int s, int len) {
        if ((len) % 2 != 0) {
            return;
        }
        len += s;
        byte bb;
        for (int i = s; i < len; i += 2) {
            bb = b[i];
            b[i] = b[i + 1];
            b[i + 1] = bb;
        }
    }
