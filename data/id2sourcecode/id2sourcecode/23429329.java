    public synchronized int read(byte[] b, int offset, int len) {
        while (!noMore && readPtr == writePtr) {
            try {
                readerBlocked = true;
                log.debug(this + ": Waiting for data");
                wait();
                readerBlocked = false;
            } catch (InterruptedException e) {
            }
        }
        if (writerBlocked) notify();
        int l = -1;
        if (readPtr < writePtr) {
            l = Math.min(len, writePtr - readPtr);
        } else if (readPtr > writePtr) {
            l = Math.min(len, bufferSize - readPtr);
        }
        if (l > 0) {
            System.arraycopy(buffer, readPtr, b, offset, l);
            readPtr += l;
            if (readPtr == bufferSize) {
                readPtr = 0;
            }
        }
        return l;
    }
