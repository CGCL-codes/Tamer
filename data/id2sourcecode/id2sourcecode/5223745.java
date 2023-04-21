    public void run() {
        readPtr = 0;
        writePtr = 0;
        int numBytesInBuffer, offset;
        while (!disconnected) {
            synchronized (this) {
                if (writePtr == readPtr) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (disconnected) return;
                offset = readPtr;
                if (writePtr >= readPtr) numBytesInBuffer = writePtr - readPtr; else numBytesInBuffer = BUFFER_SIZE - readPtr;
            }
            if (numBytesInBuffer > 0) {
                try {
                    out.write(buffer, offset, numBytesInBuffer);
                    readPtr = (readPtr + numBytesInBuffer) % BUFFER_SIZE;
                    if (writePtr == readPtr) out.flush();
                } catch (Exception e) {
                    destruct();
                }
            }
        }
    }
