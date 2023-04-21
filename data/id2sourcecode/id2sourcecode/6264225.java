    protected final AceMessageInterface waitMessage() {
        if (messageQueue == null) {
            writeErrorMessage("The thread has not enabled message queue", null);
            return null;
        }
        synchronized (remLock) {
            synchronized (messageQueue) {
                if (messageQueue.size() > 0) {
                    try {
                        return (AceMessageInterface) messageQueue.removeFirst();
                    } catch (NoSuchElementException ex1) {
                        writeErrorMessage("Element not found in queue", null);
                        return null;
                    }
                } else {
                    while (true) {
                        try {
                            messageQueue.wait();
                            break;
                        } catch (InterruptedException ex) {
                        }
                    }
                    try {
                        return (AceMessageInterface) messageQueue.removeFirst();
                    } catch (NoSuchElementException ex1) {
                        writeErrorMessage("Element not found in queue", null);
                        return null;
                    }
                }
            }
        }
    }
