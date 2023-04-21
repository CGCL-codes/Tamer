    public synchronized void readLock() {
        Thread currentThread = Thread.currentThread();
        if (writeLockOwner == currentThread) {
            return;
        }
        Integer readLockCount = (Integer) readLockOwners.get(currentThread);
        if (readLockCount != null) {
            readLockOwners.put(currentThread, Integer.valueOf(readLockCount.intValue() + 1));
            return;
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
            } catch (InterruptedException exp) {
                wasInterrupted = true;
            }
        }
        readLockOwners.put(currentThread, Integer.valueOf(1));
        if (wasInterrupted) {
            currentThread.interrupt();
        }
    }
