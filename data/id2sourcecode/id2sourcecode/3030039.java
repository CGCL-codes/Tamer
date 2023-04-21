    public void shutdown() {
        if (writeThread.isShutdown()) {
            return;
        }
        try {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
        } catch (IllegalStateException ex) {
        }
        writeThread.trigger();
        try {
            writeThread.shutdown();
        } catch (InterruptedException iex) {
        }
    }
