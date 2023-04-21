    public boolean fillBuffer(int byteCount) throws IOException {
        buffer.reset();
        int transferred = buffer.transferFrom(stream, byteCount);
        if (transferred < byteCount) {
            if (transferred == 0 && (!eof)) {
                eof = true;
                return false;
            }
            throw new EOFException(text.get("prematureEOF", byteCount, transferred));
        }
        buffer.setPosition(0);
        return true;
    }
