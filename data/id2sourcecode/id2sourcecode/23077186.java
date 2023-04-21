    public static void swapBytes(byte[] b, int off, int len) {
        byte tempByte;
        for (int i = off; i < (off + len); i += 2) {
            tempByte = b[i];
            b[i] = b[i + 1];
            b[i + 1] = tempByte;
        }
    }
