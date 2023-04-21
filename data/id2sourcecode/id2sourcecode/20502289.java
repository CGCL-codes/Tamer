    public synchronized void readUnlock() throws IllegalAccessException {
        Thread currentThread = Thread.currentThread();
        if (writeLockOwner == currentThread) {
            return;
        }
        Integer readLockCount = (Integer) readLockOwners.remove(currentThread);
        if (readLockCount == null) {
            throw new IllegalAccessException("Thread without holding read lock trys to unlock.");
        }
        int newCount = readLockCount.intValue() - 1;
        if (newCount > 0) {
            readLockOwners.put(currentThread, Integer.valueOf(newCount));
        } else if (readLockOwners.size() == 0) {
            notifyAll();
        }
    }
