    public static final long copyReaderToWriter(Reader in, Writer out, char[] copyBuf, int offset, int len, long copySize) throws IOException {
        if ((null == in) || (null == out) || (null == copyBuf) || (offset < 0) || (len <= 0) || ((offset + len) > copyBuf.length)) return (-1L);
        long curCopy = 0L;
        for (; ; ) {
            int reqRead = len;
            if (copySize > 0L) {
                if (curCopy >= copySize) break;
                final long remCopy = copySize - curCopy;
                if (remCopy < len) reqRead = (int) remCopy;
            }
            final int readLen = in.read(copyBuf, offset, reqRead);
            if (readLen < 0) break;
            if (readLen > 0) {
                out.write(copyBuf, offset, readLen);
                curCopy += readLen;
            }
        }
        return curCopy;
    }
