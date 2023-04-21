    public synchronized void write(byte[] in, int pos, int len) throws IOException {
        int toCopy;
        while (len > 0) {
            if (bReadClosed || bWriteClosed) {
                throw new IOException("IOQueue closed");
            }
            if (bFull) {
                notify();
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new IOException("IOQueue write interrupted");
                }
                continue;
            }
            if (readPos <= writePos) {
                toCopy = BUFF_SIZE - writePos;
            } else {
                toCopy = readPos - writePos;
            }
            if (toCopy > len) {
                toCopy = len;
            }
            System.arraycopy(in, pos, buff, writePos, toCopy);
            pos += toCopy;
            writePos += toCopy;
            len -= toCopy;
            if (writePos >= BUFF_SIZE) {
                writePos = 0;
            }
            if (readPos == writePos) {
                bFull = true;
            }
        }
        notify();
    }
