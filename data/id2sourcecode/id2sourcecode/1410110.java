    void init() {
        synchronized (this) {
            lastPos = firstPos = safePos = 0;
            readPos = writePos = 0;
            accept = 0;
            lastWriteTime = 0;
        }
    }
