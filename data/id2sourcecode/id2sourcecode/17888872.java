    public void restore() {
        writePos = markWritePos;
        len = writePos - readPos;
        if (len < 0) len += size;
    }
