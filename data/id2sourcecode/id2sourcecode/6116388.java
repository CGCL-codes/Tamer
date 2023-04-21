    public int getFromBuffer(byte[] b, int off, int len) {
        int nbrOfBytesRead = 0;
        synchronized (mBuffer) {
            int readIndex = mCurrentReadPosition;
            int writeIndex = off;
            if (mCurrentReadPosition >= mCurrentWritePosition) {
                while (readIndex < mBuffer.length && nbrOfBytesRead < len) {
                    b[writeIndex] = mBuffer[readIndex];
                    readIndex++;
                    writeIndex++;
                    nbrOfBytesRead++;
                }
                if (nbrOfBytesRead < len) readIndex = 0;
            }
            while (readIndex < mCurrentWritePosition && nbrOfBytesRead < len) {
                b[writeIndex] = mBuffer[readIndex];
                readIndex++;
                writeIndex++;
                nbrOfBytesRead++;
            }
            mCurrentReadPosition = (readIndex < mBuffer.length ? readIndex : 0);
            mAvailableSpace += nbrOfBytesRead;
        }
        return nbrOfBytesRead;
    }
