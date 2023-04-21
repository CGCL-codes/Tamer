    public void open() throws IOException, SecurityException {
        PushSourceStream[] streams = source.getStreams();
        source.connect();
        writerThread = new WriterThread(source.getStreams()[0], os);
        writerThread.setName("WriterThread for " + os);
        writerThread.setDaemon(true);
    }
