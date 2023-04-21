    public static void transfer(InputStream is, OutputStream os, int bufferSize) throws IOException {
        byte transferBuf[] = new byte[bufferSize];
        int readCount;
        while ((readCount = is.read(transferBuf)) != -1) {
            os.write(transferBuf, 0, readCount);
            os.flush();
        }
    }
