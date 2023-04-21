    @Override
    protected void doPutString(String str) throws IOException {
        if (mMode == FileRef.FILEMODE_READ) throw new IOException("tried to write to a read-only MemoryBuffer");
        final int len = Math.min(str.length(), mBuffer.length - mPos);
        if (len == 0) return;
        for (int i = 0; i < len; i++) mBuffer[mPos++] = (byte) str.charAt(i);
    }
