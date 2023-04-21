    public int read(byte[] b, int off, int len) throws IOException {
        int left = blockSize - byteCount;
        int c;
        if ((c = in.read(b, off, len < left ? len : left)) == -1) {
            return -1;
        }
        md.update(b, off, c);
        byteCount += c;
        if (byteCount == blockSize) {
            digestList.add(new Buffer(md.digest()));
            byteCount = 0;
        }
        return c;
    }
