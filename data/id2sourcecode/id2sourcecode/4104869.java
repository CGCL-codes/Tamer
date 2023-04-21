    public void start() throws IOException {
        if (source != null) source.start();
        if (writeThread == null) {
            writeThread = new Thread(this);
            writeThread.setName("DataSourceHandler Thread");
            writeThread.start();
        }
        setState(STARTED);
    }
