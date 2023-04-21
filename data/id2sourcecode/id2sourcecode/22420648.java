    @Override
    protected void startupImpl() {
        Logger.debug("Starting " + this + ". Registry: " + Registry.getCurrentRegistry());
        readThread = new ReadThread();
        writeThread = new WriteThread();
        readThread.start();
        writeThread.start();
    }
