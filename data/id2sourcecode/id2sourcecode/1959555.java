    public void processBytes(InputStream is, OutputStream os, FileProcessorEnvironment env) throws IOException {
        int readByte;
        int count = 0;
        int size = getSize();
        while (((readByte = is.read()) >= 0) && env.shouldContinue()) {
            if (count < size) {
                os.write(readByte);
            }
            bytesProcessed(1);
            count++;
        }
    }
