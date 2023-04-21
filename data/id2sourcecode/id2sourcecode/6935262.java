    public boolean write(byte[] source, int offset, int length) {
        assert length < buffer.capacity() : "The requested write is bigger than the buffer";
        buffer.position(writeIndex);
        if ((readIndex <= writeIndex && writeIndex + length < buffer.capacity()) || (writeIndex < readIndex && length < readIndex - writeIndex)) {
            buffer.put(source, offset, length);
            writeIndex += length;
            return true;
        } else {
            int remainder = buffer.remaining();
            if (readIndex < writeIndex && length > readIndex + remainder) {
                return false;
            }
            if (writeIndex < readIndex && length > readIndex - writeIndex) {
                return false;
            }
            buffer.put(source, offset, remainder);
            offset += remainder;
            length -= remainder;
            writeIndex = 0;
            buffer.position(writeIndex);
            assert length < readIndex : "There is not enough room for this write operation";
            buffer.put(source, offset, length);
            writeIndex += length;
            return true;
        }
    }
