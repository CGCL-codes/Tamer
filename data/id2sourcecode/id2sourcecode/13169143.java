        public void lock() {
            if (writeLock.broken) writeLock.lockAlreadyBroken();
            readLocks.incrementAndGet();
            MutableInt myCount = myReadLocks.get();
            myCount.count++;
            Thread threadOwningLock = threadHoldingLock.get();
            if (threadOwningLock != null && threadOwningLock != Thread.currentThread()) {
                writeLock.broken = true;
                throw new UnexpectedConcurrentAccessException("Unable to get read lock, write lock already held", threadOwningLock);
            }
        }
