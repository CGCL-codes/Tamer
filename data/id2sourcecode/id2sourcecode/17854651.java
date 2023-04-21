    private synchronized boolean readLock() throws LockException {
        if (writeLockedThread == Thread.currentThread()) {
            outstandingReadLocks++;
            return true;
        }
        waitingForReadLock++;
        while (writeLockedThread != null) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                throw new LockException("Interrupted while waiting for read lock");
            }
        }
        waitingForReadLock--;
        outstandingReadLocks++;
        return true;
    }
