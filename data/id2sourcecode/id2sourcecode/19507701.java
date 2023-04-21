    public void run() {
        packetSize = 0;
        packetType = -1;
        readPtr = 0;
        writePtr = 0;
        int numBytesInBuffer, offset;
        while (!die) {
            synchronized (this) {
                if (writePtr == readPtr) {
                    try {
                        wait();
                    } catch (InterruptedException _ex) {
                    }
                }
                if (die) return;
                offset = readPtr;
                if (writePtr >= readPtr) numBytesInBuffer = writePtr - readPtr; else numBytesInBuffer = bufferSize - readPtr;
            }
            if (numBytesInBuffer > 0) {
                try {
                    out.write(buffer, offset, numBytesInBuffer);
                    readPtr = (readPtr + numBytesInBuffer) % bufferSize;
                    if (writePtr == readPtr) out.flush();
                } catch (Exception _ex) {
                    kill("buffer write exception - " + _ex.getMessage());
                }
            }
        }
    }
