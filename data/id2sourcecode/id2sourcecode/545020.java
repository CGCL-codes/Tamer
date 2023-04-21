            public void run() {
                threadAssertFalse(lock.writeLock().tryLock());
            }
