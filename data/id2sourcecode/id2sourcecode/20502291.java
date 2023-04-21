    public synchronized void writeUnlock() throws IllegalAccessException {
        Thread currentThread = Thread.currentThread();
        if (writeLockOwner != currentThread) {
            throw new IllegalAccessException("Current thread not owner of write lock.");
        }
        writeLockOwner = null;
        notifyAll();
    }
