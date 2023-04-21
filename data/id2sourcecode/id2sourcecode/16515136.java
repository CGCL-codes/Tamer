    public void readBytes(byte[] dst, int dstIndex, int length) {
        checkReadableBytes(length);
        int readTotalLen = 0;
        while (readTotalLen < length) {
            ByteArray buffer = getReadBuffer();
            int bufoff = getReadBufferOffset();
            int remaining = buffer.capacity() - bufoff;
            int writeLen = remaining < (length - readTotalLen) ? remaining : (length - readTotalLen);
            System.arraycopy(buffer.array, buffer.off + bufoff, dst, dstIndex, writeLen);
            readTotalLen += writeLen;
            dstIndex += writeLen;
            readerIndex += writeLen;
        }
    }
