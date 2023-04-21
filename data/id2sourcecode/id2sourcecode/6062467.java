    public int computeBufferSize(javax.media.format.AudioFormat f) {
        long bytesPerSecond = (long) (f.getSampleRate() * f.getChannels() * f.getSampleSizeInBits() / 8);
        long bufSize;
        long bufLen;
        if (bufLenReq < DefaultMinBufferSize) bufLen = DefaultMinBufferSize; else if (bufLenReq > DefaultMaxBufferSize) bufLen = DefaultMaxBufferSize; else bufLen = bufLenReq;
        float r = bufLen / 1000f;
        bufSize = (long) (bytesPerSecond * r);
        return (int) bufSize;
    }
