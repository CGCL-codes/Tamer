    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) throw new NullPointerException();
        if ((off < 0) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0) || (off > b.length)) throw new IndexOutOfBoundsException();
        if (len == 0) return 0;
        if (isEOF) return -1;
        int ret = c.cm.getChannelData(c, extendedFlag, b, off, len);
        if (ret == -1) {
            isEOF = true;
        }
        return ret;
    }
