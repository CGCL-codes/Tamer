    public synchronized int available() {
        int l = 0;
        if (noMore) return Integer.MAX_VALUE;
        if (readPtr < writePtr) {
            l = writePtr - readPtr;
        } else if (readPtr > writePtr) {
            l = bufferSize - readPtr + writePtr;
        }
        return l;
    }
