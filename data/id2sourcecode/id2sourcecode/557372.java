    public void getReadLock() {
        synchronized (lockObj) {
            while ((writeLockIssued) || (threadsWaitingForWriteLock != 0)) {
                try {
                    lockObj.wait();
                } catch (InterruptedException e) {
                }
            }
            totalReadLocksGiven++;
        }
    }
