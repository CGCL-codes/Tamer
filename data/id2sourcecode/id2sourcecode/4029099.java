    public synchronized void writeLock() throws InterruptedException {
        Thread me = Thread.currentThread();
        Count myReadLocks = (Count) readLocksByThread.get();
        if (myReadLocks.value > 0) {
            throw new IllegalStateException(LOCALISER.msg("030001"));
        }
        if (writeLockedBy != me) {
            while (writeLocks > 0 || readLocks > 0) {
                wait(WAIT_LOG_INTERVAL);
                if (writeLocks > 0 || readLocks > 0) {
                    if (NucleusLogger.GENERAL.isDebugEnabled()) {
                        NucleusLogger.GENERAL.debug(LOCALISER.msg("030002", this), new InterruptedException());
                    }
                }
            }
            writeLockedBy = me;
        }
        ++writeLocks;
    }
