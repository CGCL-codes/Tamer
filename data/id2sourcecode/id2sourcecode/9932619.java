    public void read(byte[] destbuffer, int offset, int length) throws IOException {
        assert length < tempbuffer.length : "The requested data is bigger than the tempbuffer";
        if (readIndex + length >= writeIndex) {
            if (0 == readData()) {
                throw new EOFException();
            }
        }
        buffer.position(readIndex);
        buffer.get(destbuffer, offset, length);
        readIndex += length;
    }
