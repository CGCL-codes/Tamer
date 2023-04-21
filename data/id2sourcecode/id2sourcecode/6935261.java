    public int read(byte[] dest, int offset, int length) {
        assert length < buffer.capacity() : "The requested read is bigger than the buffer";
        if (writeIndex == readIndex) {
            return 0;
        }
        buffer.position(readIndex);
        if (writeIndex < readIndex) {
            int remainder = buffer.remaining();
            if (remainder < length) {
                buffer.get(dest, offset, remainder);
                offset += remainder;
                length -= remainder;
                readIndex = 0;
                buffer.position(readIndex);
                int space = writeIndex - readIndex;
                if (space <= length) {
                    length = space;
                }
                buffer.get(dest, offset, length);
                readIndex += length;
                return remainder + length;
            } else {
                buffer.get(dest, offset, remainder);
                readIndex += remainder;
                return remainder;
            }
        } else {
            int space = writeIndex - readIndex;
            if (space <= length) {
                length = space;
            }
            buffer.get(dest, offset, length);
            readIndex += length;
            return length;
        }
    }
