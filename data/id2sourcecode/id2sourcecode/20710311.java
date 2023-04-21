    public synchronized int read(ByteBuffer buff, long pos) throws IOException {
        if (readBuffer == null) {
            readBuffer = ByteBuffer.allocateDirect(capacity);
            readBufferStartPosition = Long.MIN_VALUE;
        }
        int rc = buff.remaining();
        while (buff.remaining() > 0) {
            if (writeBuffer != null && writeBufferStartPosition <= pos) {
                long positionInBuffer = pos - writeBufferStartPosition;
                long bytesToCopy = writeBuffer.position() - positionInBuffer;
                if (bytesToCopy > buff.remaining()) {
                    bytesToCopy = buff.remaining();
                }
                if (bytesToCopy == 0) {
                    throw new IOException("Read past EOF");
                }
                ByteBuffer src = writeBuffer.duplicate();
                src.position((int) positionInBuffer);
                src.limit((int) (positionInBuffer + bytesToCopy));
                buff.put(src);
                pos += bytesToCopy;
            } else if (readBufferStartPosition <= pos && pos < readBufferStartPosition + readBuffer.capacity()) {
                long positionInBuffer = pos - readBufferStartPosition;
                long bytesToCopy = readBuffer.capacity() - positionInBuffer;
                if (bytesToCopy > buff.remaining()) {
                    bytesToCopy = buff.remaining();
                }
                ByteBuffer src = readBuffer.duplicate();
                src.position((int) positionInBuffer);
                src.limit((int) (positionInBuffer + bytesToCopy));
                buff.put(src);
                pos += bytesToCopy;
            } else {
                readBufferStartPosition = pos;
                readBuffer.clear();
                if (readBufferStartPosition + readBuffer.capacity() >= writeBufferStartPosition) {
                    readBufferStartPosition = writeBufferStartPosition - readBuffer.capacity();
                    if (readBufferStartPosition < 0) {
                        readBuffer.put(LedgerEntryPage.zeroPage, 0, (int) -readBufferStartPosition);
                    }
                }
                while (readBuffer.remaining() > 0) {
                    if (bc.read(readBuffer, readBufferStartPosition + readBuffer.position()) <= 0) {
                        throw new IOException("Short read");
                    }
                }
                readBuffer.put(LedgerEntryPage.zeroPage, 0, readBuffer.remaining());
                readBuffer.clear();
            }
        }
        return rc;
    }
