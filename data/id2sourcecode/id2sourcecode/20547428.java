    @Override
    protected String doGetLine(int maxLen) throws IOException {
        if (mMode == FileRef.FILEMODE_WRITE) throw new IOException("tried to read from write-only MemoryBuffer");
        int end = mPos + maxLen;
        if (end > mBuffer.length) end = mBuffer.length;
        byte[] result = new byte[end - mPos];
        for (int i = 0; mPos != end; mPos++) if ((result[i++] = mBuffer[mPos]) == '\n') break;
        return new String(result);
    }
