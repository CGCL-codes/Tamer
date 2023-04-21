        public void unlock() {
            if (broken) lockWasContended();
            writeLocks--;
            if (writeLocks == 0) threadHoldingLock.set(null);
            int myReadLockCount = myReadLocks.get().count;
            int totalReadLockCount = readLocks.get();
            if (myReadLockCount < totalReadLockCount) {
                broken = true;
                throw new UnexpectedConcurrentAccessException("Read lock obtained by another thread this thread held write lock");
            }
        }
