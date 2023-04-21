    public synchronized boolean write(byte[] source, int offset, int length) {
        assert length < buffer.capacity() : "The requested write is bigger than the buffer";
        buffer.position(writeIndex);
        if (length < buffer.capacity() - getBytesInBuffer()) {
            if (writeIndex <= readIndex) {
                assert (readIndex - writeIndex) == (buffer.capacity() - getBytesInBuffer()) : "Buffer size is invalid";
                buffer.put(source, offset, length);
                writeIndex += length;
                return true;
            } else {
                int remainder = buffer.remaining();
                if (length < remainder) {
                    buffer.put(source, offset, length);
                    writeIndex += length;
                    return true;
                } else {
                    buffer.put(source, offset, remainder);
                    buffer.position(0);
                    buffer.put(source, offset + remainder, length - remainder);
                    writeIndex = length - remainder;
                    return true;
                }
            }
        } else {
            int remainder = buffer.remaining();
            if (length < remainder) {
                buffer.put(source, offset, length);
                writeIndex += length;
            } else {
                buffer.put(source, offset, remainder);
                buffer.position(0);
                buffer.put(source, offset + remainder, length - remainder);
                writeIndex = length - remainder;
            }
            int oldRead = readIndex;
            readIndex = writeIndex + 1;
            if (readIndex >= buffer.capacity()) {
                readIndex -= buffer.capacity();
            }
            if (readIndex < oldRead) {
                readOffset += buffer.capacity() - oldRead + readIndex;
            } else {
                readOffset += readIndex - oldRead;
            }
            return true;
        }
    }
