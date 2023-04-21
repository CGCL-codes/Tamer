    public synchronized int read() throws IOException {
        while (true) {
            if (bReadClosed) {
                throw new IOException("IOQueue closed");
            }
            if (readPos == writePos && !bFull) {
                if (bWriteClosed) {
                    return -1;
                } else {
                    notify();
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new IOException("IOQueue read() interrupted");
                    }
                    continue;
                }
            }
            int i = buff[readPos++] & 0xFF;
            if (readPos >= BUFF_SIZE) {
                readPos = 0;
            }
            if (bFull) {
                bFull = false;
                notify();
            }
            return i;
        }
    }
