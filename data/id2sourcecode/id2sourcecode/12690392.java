    protected void compact() {
        if (readPos == 0) return;
        int avail = writePos - readPos;
        System.arraycopy(buf, readPos, buf, 0, avail);
        readPos = 0;
        writePos = avail;
    }
