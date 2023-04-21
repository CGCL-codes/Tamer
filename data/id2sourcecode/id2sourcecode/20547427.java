    @Override
    protected int doGetChar() throws IOException {
        if (mMode == FileRef.FILEMODE_WRITE) throw new IOException("tried to read from write-only MemoryBuffer");
        if (mPos >= mBuffer.length) return -1; else return mBuffer[mPos++];
    }
