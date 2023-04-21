    public void afterPropertiesSet() throws Exception {
        writerThread = new IndexWriterThread(60000);
        writerThread.start();
    }
