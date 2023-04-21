    @Override
    public void process(InputStream is, OutputStream os, FileProcessorEnvironment env) throws IOException {
        byte[] buffer = new byte[1024];
        int readCount;
        while (((readCount = is.read(buffer)) >= 0) && env.shouldContinue()) {
            os.write(buffer, 0, readCount);
            bytesProcessed(readCount);
        }
    }
