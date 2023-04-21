    private synchronized void releaseWrite() {
        if (Thread.currentThread() == writeLockedThread) {
            if (outstandingWriteLocks > 0) outstandingWriteLocks--;
            if (outstandingWriteLocks > 0) {
                return;
            }
            if (outstandingReadLocks == 0 && waitingForWriteLock != null && waitingForWriteLock.size() > 0) {
                writeLockedThread = (Thread) waitingForWriteLock.get(0);
                synchronized (writeLockedThread) {
                    writeLockedThread.notify();
                }
            } else {
                writeLockedThread = null;
                if (waitingForReadLock > 0) {
                    notifyAll();
                } else {
                }
            }
        } else {
            log.warn("Illegal lock usage: thread does not hold the write lock");
            throw new IllegalStateException("Thread does not have lock");
        }
    }
