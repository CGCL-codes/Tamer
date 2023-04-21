    public void disconnect() {
        QueueProcessorThread old = writeThread;
        writeThread = null;
        shutdownConnection();
        if (old != null) {
            old.kill();
        }
    }
