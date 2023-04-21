    public synchronized void setBufferSize(int size) {
        if (size <= bufferSize) return;
        byte[] b = new byte[size];
        if (readPtr < writePtr) {
            int l = writePtr - readPtr;
            System.arraycopy(buffer, readPtr, b, 0, l);
            readPtr = 0;
            writePtr = l;
        } else if (readPtr > writePtr) {
            int l;
            System.arraycopy(buffer, readPtr, b, 0, bufferSize - readPtr);
            System.arraycopy(buffer, 0, b, bufferSize - readPtr, writePtr);
            writePtr = bufferSize - readPtr + writePtr;
            readPtr = 0;
        }
        buffer = b;
        bufferSize = size;
    }
