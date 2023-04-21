    private void shutdownWriter() {
        keepRunning = false;
        if (writeThread == null || !writeThread.isAlive()) return;
        writeThread.interrupt();
        try {
            writeThread.join();
        } catch (InterruptedException e) {
            syslog.log(Level.SEVERE, "Interrupted while waiting for write thread to join. Data file may not be saved.", e);
        }
    }
