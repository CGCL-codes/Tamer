    public final boolean sendHighPriortyMessage(AceMessageInterface msg) {
        if (messageQueue == null) {
            writeErrorMessage("The thread has not enabled message queue", null);
            return false;
        }
        synchronized (messageQueue) {
            messageQueue.addFirst(msg);
            messageQueue.notify();
        }
        return true;
    }
