    public byte read() throws IOException {
        if (readIndex >= writeIndex) {
            if (0 == readData()) {
                throw new EOFException();
            }
        }
        return buffer.get(readIndex++);
    }
