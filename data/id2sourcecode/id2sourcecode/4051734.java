    public synchronized int read(int position, byte[] dest, int destOffset, int destLength) {
        assert destLength < buffer.capacity() : "The requested read is bigger than the buffer";
        if (position < readOffset) {
            throw new InvalidBufferPositionException("Read position " + position + " is smaller than [" + readOffset + "]");
        }
        int offset = position - readOffset;
        if (offset > getBytesInBuffer()) {
            throw new InvalidBufferPositionException("Read position " + position + " is larger then [" + (readOffset + getBytesInBuffer()) + "]");
        }
        if (writeIndex == readIndex) {
            return 0;
        }
        buffer.position(offset);
        if (writeIndex < readIndex) {
            int remainder = buffer.remaining();
            if (remainder < destLength) {
                buffer.get(dest, destOffset, remainder);
                destOffset += remainder;
                destLength -= remainder;
                buffer.position(0);
                int space = writeIndex - 0;
                if (space <= destLength) {
                    destLength = space;
                }
                buffer.get(dest, destOffset, destLength);
                return remainder + destLength;
            } else {
                buffer.get(dest, destOffset, remainder);
                return remainder;
            }
        } else {
            int space = writeIndex - offset;
            if (space <= destLength) {
                destLength = space;
            }
            buffer.get(dest, destOffset, destLength);
            return destLength;
        }
    }
