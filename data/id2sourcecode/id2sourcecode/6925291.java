    public int transferFrom(ByteBuffer srcbuf, int maxbytes) throws IOException {
        checkNotClosed();
        int transferCount = 0;
        synchronized (blocks) {
            ByteBuffer buf = blocks.isEmpty() ? addNewBuffer() : (ByteBuffer) blocks.getLast();
            boolean firstBuffer = blocks.size() == 1;
            if (firstBuffer && readMode) {
                enterWriteMode(buf);
            }
            while (transferCount < maxbytes) {
                int maxread = maxbytes - transferCount;
                boolean bufferTooLarge = buf.remaining() > maxread;
                if (bufferTooLarge) {
                    buf.limit(buf.position() + maxread);
                }
                int posBefore = buf.position();
                buf.put(srcbuf);
                int read = buf.position() - posBefore;
                if (bufferTooLarge) {
                    buf.limit(buf.capacity());
                }
                if (!buf.hasRemaining()) {
                    buf.flip();
                    buf = addNewBuffer();
                }
                if (read == 0) break;
                if (read == -1) {
                    if (transferCount == 0) transferCount = -1;
                    break;
                }
                available += read;
                transferCount += read;
            }
            if (buf.position() == 0) {
                releaseBuffer(buf);
            }
            if (transferCount > 0) afterWrite();
            return transferCount;
        }
    }
