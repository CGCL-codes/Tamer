    public final boolean removeMessage(AceMessageInterface obj, AceCompareMessageInterface comp) {
        boolean removed = false;
        if (messageQueue == null) {
            writeErrorMessage("The thread has not enabled message queue", null);
            return false;
        }
        synchronized (remLock) {
            synchronized (messageQueue) {
                ListIterator iter = messageQueue.listIterator(0);
                while (iter.hasNext() == true) {
                    AceMessageInterface msg = (AceMessageInterface) iter.next();
                    if (comp.same(obj, msg) == true) {
                        iter.remove();
                        removed = true;
                    }
                }
            }
        }
        return removed;
    }
