    public void close() {
        synchronized (gate) {
            selector.wakeup();
            running = false;
            if (key.isValid()) {
                key.attach(null);
                key.cancel();
            }
        }
        try {
            ssc.close();
            selector.close();
        } catch (IOException e) {
            log.error("Exception while closing server socket", e);
        }
        stopThreads(readers);
        stopThreads(writers);
    }
