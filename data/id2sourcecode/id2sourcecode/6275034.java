    public void start() throws IOException {
        if (state == OPENED) {
            if (source != null) source.start();
            if (writeThread == null) {
                writeThread = new Thread(this);
                writeThread.start();
            }
            setState(STARTED);
        }
    }
