    private boolean writeLock() throws LockException {
        Thread thisThread = Thread.currentThread();
        synchronized (this) {
            if (writeLockedThread == null && outstandingReadLocks == 0) {
                writeLockedThread = Thread.currentThread();
                outstandingWriteLocks++;
                return true;
            }
            if (waitingForWriteLock == null) waitingForWriteLock = new ArrayList(3);
            waitingForWriteLock.add(thisThread);
        }
        synchronized (thisThread) {
            while (thisThread != writeLockedThread) {
                try {
                    thisThread.wait();
                } catch (InterruptedException e) {
                    throw new LockException("Interrupted");
                }
            }
            outstandingWriteLocks++;
        }
        synchronized (this) {
            int i = waitingForWriteLock.indexOf(thisThread);
            waitingForWriteLock.remove(i);
        }
        return true;
    }
