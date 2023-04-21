    public ByteBuffer readByteBuffer(int length) {
        checkReadableBytes(length);
        ByteArray buffer = getReadBuffer();
        int bufoff = getReadBufferOffset();
        int remaining = buffer.capacity() - bufoff;
        if (remaining >= length) {
            return ByteBuffer.wrap(buffer.array, buffer.off + bufoff, length);
        }
        int readTotalLen = 0;
        ByteBuffer ret = ByteBuffer.allocate(length);
        while (readTotalLen < length) {
            int writeLen = remaining < (length - readTotalLen) ? remaining : (length - readTotalLen);
            ret.put(buffer.array, buffer.off + bufoff, writeLen);
            readTotalLen += writeLen;
            buffer = getReadBuffer();
            bufoff = getReadBufferOffset();
            remaining = buffer.capacity() - bufoff;
            readerIndex += writeLen;
        }
        ret.flip();
        return ret;
    }
