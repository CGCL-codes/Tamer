    public void startSessionThreads() {
        readerThread = new Thread(serverReader, "SAWClientServerReader");
        readerThread.setPriority(Thread.NORM_PRIORITY);
        readerThread.start();
        writerThread = new Thread(clientWriter, "SAWClientServerWriter");
        writerThread.setPriority(Thread.NORM_PRIORITY);
        writerThread.start();
    }
