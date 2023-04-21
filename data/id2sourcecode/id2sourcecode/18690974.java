    public void returnExclusiveWriteLock() {
        if (lockOwner == Thread.currentThread()) {
            model.leaveCriticalSection();
            lockOwner = null;
            if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getId() + " returned exclusive write lock for " + this + ".");
        } else throw new RuntimeException("Attempt to return exclusive write lock by another thread.");
    }
