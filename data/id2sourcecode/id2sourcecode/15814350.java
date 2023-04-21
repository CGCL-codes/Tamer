    public static void copyStream(InputStream in, OutputStream out, int bufSize) throws IOException {
        bufSize = bufSize <= 0 ? DEFAULT_BUFSIZE : bufSize;
        byte[] buf = new byte[bufSize];
        int readCount = in.read(buf);
        while (readCount > 0) {
            out.write(buf, 0, readCount);
            readCount = in.read(buf);
        }
    }
