    public int currentlyInBuffer() {
        assert writePos >= readPos;
        return writePos - readPos;
    }
