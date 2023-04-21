    public void wakeThreads() {
        readThread.interrupt();
        writeThread.interrupt();
    }
