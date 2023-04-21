    public static void copyChars(Reader in, Writer out, int bufSize) throws IOException {
        bufSize = bufSize <= 0 ? DEFAULT_BUFSIZE : bufSize;
        char[] buf = new char[bufSize];
        int readCount = in.read(buf);
        while (readCount > 0) {
            out.write(buf, 0, readCount);
            readCount = in.read(buf);
        }
    }
