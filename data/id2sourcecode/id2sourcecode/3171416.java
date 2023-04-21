    public synchronized int available() {
        if (bFull) {
            return BUFF_SIZE;
        }
        if (readPos == writePos && !bFull) {
            return 0;
        }
        if (writePos <= readPos) {
            return BUFF_SIZE - readPos;
        } else {
            return writePos - readPos;
        }
    }
