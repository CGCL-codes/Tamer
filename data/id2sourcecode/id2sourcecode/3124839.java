    public void releaseLock() {
        synchronized (mutex) {
            Thread currentThread = Thread.currentThread();
            if (currentLocks == 0) throw new IllegalStateException(currentThread + ": currentLocks == 0: releaseLock called without previous acquire!");
            ArrayList lockStack = (ArrayList) lockStacksByThread.get(currentThread);
            if (lockStack == null) throw new IllegalStateException(currentThread + ": No lock stack registered for current thread!");
            if (lockStack.isEmpty()) throw new IllegalStateException(currentThread + ": lock stack of current thread is empty!");
            Boolean releaseWriteLock = (Boolean) lockStack.remove(lockStack.size() - 1);
            if (releaseWriteLock.booleanValue()) {
                if (currentLocks > 0) throw new IllegalStateException(currentThread + ": currentLocks > 0, but we are trying to release a write lock!");
                currentLocks++;
                if (currentLocks == 0) {
                    currentWriteThread = null;
                    ReadLockCount rlc = savedReadLocks.remove(currentThread);
                    if (rlc != null) {
                        if (rlc.readLockCount > 0) {
                            currentLocks += rlc.readLockCount;
                            currentReadThreads.put(currentThread, rlc);
                            rwLockMan.acquireLock(this, RWLockMan.MODE_READ, rlc.readLockCount);
                        }
                    }
                }
                rwLockMan.releaseLock(this, RWLockMan.MODE_WRITE, 1);
            } else {
                if (currentLocks < 0) {
                    if (currentThread != currentWriteThread) throw new IllegalStateException(currentThread + ": Current thread is not current write thread! Why are we here?");
                    ReadLockCount rlc = savedReadLocks.get(currentThread);
                    if (rlc == null || rlc.readLockCount == 0) throw new IllegalStateException(currentThread + ": Current thread does not have a read lock set, but tries to release one!");
                    rlc.readLockCount--;
                } else {
                    ReadLockCount rlc = currentReadThreads.get(currentThread);
                    if (rlc == null || rlc.readLockCount == 0) throw new IllegalStateException(currentThread + ": Current thread does not have a read lock set, but tries to release one!");
                    currentLocks--;
                    rlc.readLockCount--;
                }
                rwLockMan.releaseLock(this, RWLockMan.MODE_READ, 1);
            }
            mutex.notifyAll();
        }
    }
