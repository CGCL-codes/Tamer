    public final boolean flushMessages() {
        if (messageQueue == null) {
            writeErrorMessage("The thread has not enabled message queue", null);
            return false;
        }
        synchronized (remLock) {
            synchronized (messageQueue) {
                messageQueue.clear();
            }
        }
        return true;
    }
