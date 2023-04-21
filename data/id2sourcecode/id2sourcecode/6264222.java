    public final boolean sendMessage(AceMessageInterface msg) {
        if (messageQueue == null) {
            writeErrorMessage("The thread has not enabled message queue", null);
            return false;
        }
        synchronized (messageQueue) {
            messageQueue.addLast(msg);
            messageQueue.notify();
        }
        return true;
    }
