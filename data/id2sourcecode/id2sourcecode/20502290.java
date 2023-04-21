    public synchronized void writeLock() {
        Thread currentThread = Thread.currentThread();
        if (writeLockOwner == currentThread) {
            return;
        }
        if (readLockOwners.containsKey(currentThread)) {
            throw new RuntimeException("Can't upgrade read lock, this could cause deadlocks.");
        }
        boolean wasInterrupted = false;
        while (writeLockOwner != null) {
            try {
                long startTime = System.currentTimeMillis();
                wait(MAX_WAIT_TIME);
                long stopTime = System.currentTimeMillis();
                if (stopTime >= startTime + MAX_WAIT_TIME) {
                    throwWaitedTooLongError();
                }
            } catch (InterruptedException e) {
                wasInterrupted = true;
            }
        }
        writeLockOwner = currentThread;
        while (readLockOwners.size() > 0) {
            try {
                long startTime = System.currentTimeMillis();
                wait(MAX_WAIT_TIME / 20);
                long stopTime = System.currentTimeMillis();
                if (stopTime >= startTime + MAX_WAIT_TIME) {
                    writeLockOwner = null;
                    NLogger.error(ReadWriteLock.class, "Waited too long to ensure write lock.");
                    throw new RuntimeException("Waited too long to ensure write lock.");
                }
            } catch (InterruptedException e) {
                wasInterrupted = true;
            }
        }
        if (wasInterrupted) {
            currentThread.interrupt();
        }
    }
