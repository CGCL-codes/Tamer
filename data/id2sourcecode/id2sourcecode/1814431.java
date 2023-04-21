    public void run() {
        initClasses();
        Data.load();
        resetWalkingQueue();
        packetSize = 0;
        packetType = -1;
        readPtr = 0;
        writePtr = 0;
        int numBytesInBuffer, offset;
        while (!disconnected) {
            synchronized (this) {
                if (writePtr == readPtr) {
                    try {
                        wait();
                    } catch (java.lang.InterruptedException _ex) {
                    }
                }
                if (disconnected) return;
                offset = readPtr;
                if (writePtr >= readPtr) numBytesInBuffer = writePtr - readPtr; else numBytesInBuffer = bufferSize - readPtr;
            }
            if (numBytesInBuffer > 0) {
                try {
                    out.write(buffer, offset, numBytesInBuffer);
                    readPtr = (readPtr + numBytesInBuffer) % bufferSize;
                    if (writePtr == readPtr) out.flush();
                } catch (java.lang.Exception e) {
                    disconnected = true;
                }
            }
        }
    }
