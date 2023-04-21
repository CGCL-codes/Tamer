    private void readWrite(byte[] b, int off, int len, boolean write) throws IOException {
        long end = pos + len;
        if (end > length) {
            if (write) {
                changeLength(end);
            } else {
                if (len == 0) {
                    return;
                }
                throw new EOFException("File: " + name);
            }
        }
        while (len > 0) {
            int l = (int) Math.min(len, BLOCK_SIZE - (pos & BLOCK_SIZE_MASK));
            int page = (int) (pos >>> BLOCK_SIZE_SHIFT);
            expand(data, page);
            byte[] block = data[page];
            int blockOffset = (int) (pos & BLOCK_SIZE_MASK);
            if (write) {
                System.arraycopy(b, off, block, blockOffset, l);
            } else {
                System.arraycopy(block, blockOffset, b, off, l);
            }
            if (compress) {
                compressLater(data, page);
            }
            off += l;
            pos += l;
            len -= l;
        }
    }
