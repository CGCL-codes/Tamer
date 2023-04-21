    public synchronized void stop() {
        writerThread.stop();
        readerThread.stop();
        close();
        setStatus(STATUS_DISCONNECTED);
    }
