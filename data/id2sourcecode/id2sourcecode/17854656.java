    private synchronized void releaseRead() {
        if (outstandingReadLocks > 0) {
            outstandingReadLocks--;
            if (outstandingReadLocks == 0 && writeLockedThread == null && waitingForWriteLock != null && waitingForWriteLock.size() > 0) {
                writeLockedThread = (Thread) waitingForWriteLock.get(0);
                synchronized (writeLockedThread) {
                    writeLockedThread.notifyAll();
                }
            }
            return;
        } else throw new IllegalStateException("Attempt to release a non-existing read lock.");
    }
