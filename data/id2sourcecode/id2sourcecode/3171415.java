    public synchronized int read(byte[] in, int pos, int len) throws IOException {
        if (len <= 0) {
            return 0;
        }
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
            int toCopy;
            if (writePos <= readPos) {
                toCopy = BUFF_SIZE - readPos;
            } else {
                toCopy = writePos - readPos;
            }
            if (toCopy > len) {
                toCopy = len;
            }
            System.arraycopy(buff, readPos, in, pos, toCopy);
            readPos += toCopy;
            if (readPos >= BUFF_SIZE) {
                readPos = 0;
            }
            if (bFull) {
                bFull = false;
                notify();
            }
            return toCopy;
        }
    }
