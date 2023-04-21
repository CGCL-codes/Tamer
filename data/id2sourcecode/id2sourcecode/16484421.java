    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        int first = read();
        if (first == -1) {
            return -1;
        }
        synchronized (this) {
            b[off] = (byte) (first & 0xff);
            int count = 1;
            for (int i = 1; readIndex != writeIndex && i < len; i++) {
                b[off + i] = buf[readIndex++];
                count++;
                if (readIndex == buf.length) {
                    readIndex = 0;
                }
            }
            return count;
        }
    }
