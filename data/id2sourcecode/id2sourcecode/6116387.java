    public void addToBuffer(byte[] b, int off, int len) {
        synchronized (mBuffer) {
            while (this.getAvailableBufferSpace() < len) this.resizeBuffer();
            int readIndex = off;
            int writeIndex = mCurrentWritePosition;
            while (readIndex < off + len && writeIndex < mBuffer.length) {
                mBuffer[writeIndex] = b[readIndex];
                readIndex++;
                writeIndex++;
            }
            if (readIndex < off + len) {
                writeIndex = 0;
                while (readIndex < off + len) {
                    mBuffer[writeIndex] = b[readIndex];
                    readIndex++;
                    writeIndex++;
                }
            }
            mCurrentWritePosition = (writeIndex < mBuffer.length ? writeIndex : 0);
            mAvailableSpace -= len;
        }
    }
