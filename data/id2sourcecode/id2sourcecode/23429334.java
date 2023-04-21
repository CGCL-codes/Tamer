    public synchronized void write(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int l;
            if (noMore) {
                throw new IOException("writing after close");
            }
            if (readPtr <= writePtr) {
                l = Math.min(bufferSize - writePtr - (readPtr == 0 ? 1 : 0), len);
            } else {
                l = Math.min(readPtr - writePtr - 1, len);
            }
            if (l == 0) {
                try {
                    writerBlocked = true;
                    log.debug(this + "Waiting for space");
                    wait();
                    writerBlocked = false;
                } catch (InterruptedException e) {
                }
            } else {
                System.arraycopy(b, off, buffer, writePtr, l);
                writePtr += l;
                len -= l;
                off += l;
            }
            if (writePtr == bufferSize) writePtr = 0;
            if (readerBlocked) notify();
        }
    }
