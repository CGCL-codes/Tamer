    public void requestExclusiveWriteLock() {
        while (lockOwner != null) {
            try {
                wait(WAIT_INTERVAL);
            } catch (InterruptedException ignore) {
                log.debug("Thread " + Thread.currentThread() + " is waiting for exclusive write lock for " + this + " which is still owned by Thread " + lockOwner + "...");
            }
        }
        lockOwner = Thread.currentThread();
        model.enterCriticalSection(Lock.WRITE);
        if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " obtained exclusive write lock for " + this + ".");
    }
