    protected synchronized int read() throws IOException {
        while (readIndex == writeIndex) {
            waiting = true;
            try {
                if (!closed) {
                    wait(10000);
                }
                if (closed) {
                    return -1;
                }
            } catch (InterruptedException e) {
                throw new IOException("read interrupted");
            }
        }
        int result = (int) buf[readIndex++];
        if (readIndex == buf.length) {
            readIndex = 0;
        }
        return result;
    }
