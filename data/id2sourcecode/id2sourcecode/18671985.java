    private static void runWriteThread() {
        if (writeThread.isAlive()) return;
        try {
            writeThread.interrupt();
        } catch (Exception e) {
            log.error("interrupt write thread error", e);
        }
        startNewThread();
    }
