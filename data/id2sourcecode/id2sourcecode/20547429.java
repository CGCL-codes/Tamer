    @Override
    protected void doPutChar(char c) throws IOException {
        if (mMode == FileRef.FILEMODE_READ) throw new IOException("tried to write to a read-only MemoryBuffer");
        if (mPos == mBuffer.length) return;
        mBuffer[mPos++] = (byte) c;
    }
