    public int compact(int leastLength) {
        int writableBytes = writeableBytes();
        if (writableBytes < leastLength) {
            return 0;
        }
        int readableBytes = readableBytes();
        int total = capacity();
        byte[] newSpace = null;
        if (readableBytes > 0) {
            newSpace = new byte[readableBytes];
            System.arraycopy(buffer, read_index, newSpace, 0, readableBytes);
        }
        read_index = 0;
        write_index = readableBytes;
        buffer = newSpace;
        return total - readableBytes;
    }
