    public Buffer(Buffer buffer, int offset, int length) {
        writeBytes(buffer.data, buffer.readPos + offset, length);
    }
