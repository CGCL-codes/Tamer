    public synchronized boolean isLockedForWrite() {
        return writeLockedThread != null || (waitingForWriteLock != null && waitingForWriteLock.size() > 0);
    }
