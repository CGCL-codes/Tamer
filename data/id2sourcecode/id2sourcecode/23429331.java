    public synchronized int read() throws IOException {
        if (readPtr != writePtr) {
            byte b = buffer[readPtr];
            readPtr++;
            if (readPtr == bufferSize) {
                readPtr = 0;
            }
            return b;
        }
        if (!noMore) {
            byte b[] = new byte[1];
            int l = read(b);
            if (l == 1) return b[0];
        }
        return -1;
    }
