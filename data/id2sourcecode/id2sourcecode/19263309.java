    public final void enterCriticalSection(boolean readLockRequested) {
        LockState state = getLockState();
        if (log.isDebugEnabled()) log.debug(Thread.currentThread().getName() + " >> enterCS: " + report(state));
        if (state.readLocks > 0 && state.writeLocks == 0 && !readLockRequested) {
            state.readLocks++;
            activeReadLocks.increment();
            if (log.isDebugEnabled()) log.debug(Thread.currentThread().getName() + " << enterCS: promotion attempt: " + report(state));
            throw new JenaException("enterCriticalSection: Write lock request while holding read lock - potential deadlock");
        }
        if (state.writeLocks > 0 && readLockRequested) readLockRequested = false;
        try {
            if (readLockRequested) {
                if (state.readLocks == 0) mrswLock.readLock().acquire();
                state.readLocks++;
                activeReadLocks.increment();
            } else {
                if (state.writeLocks == 0) mrswLock.writeLock().acquire();
                state.writeLocks++;
                activeWriteLocks.increment();
            }
        } catch (InterruptedException intEx) {
        } finally {
            if (log.isDebugEnabled()) log.debug(Thread.currentThread().getName() + " << enterCS: " + report(state));
        }
    }
