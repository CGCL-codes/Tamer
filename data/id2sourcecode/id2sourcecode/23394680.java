    public ReaderThread(InputStream read, String readId, OutputStream write, String writeId, Runnable afterExecute, int bufferSize) {
        this.read = read;
        this.write = write;
        this.afterExecute = afterExecute;
        this.bufferSize = bufferSize;
        this.readId = readId;
        this.writeId = writeId;
    }
