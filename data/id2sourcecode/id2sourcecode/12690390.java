    protected boolean readIntoBuffer(int minLength) {
        if (inputSource == null) {
            return false;
        }
        if (!inputSource.hasMoreData()) {
            return false;
        }
        if (bufferSpaceLeft() < minLength) {
            increaseBufferSize(minLength + currentlyInBuffer());
        } else if (buf.length - writePos < minLength) {
            compact();
        }
        int readSum = 0;
        readSum = inputSource.getData(buf, writePos, minLength);
        writePos += readSum;
        if (dataProcessor != null) {
            dataProcessor.applyInline(buf, writePos - readSum, readSum);
        }
        return readSum == minLength;
    }
