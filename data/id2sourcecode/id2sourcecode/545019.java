    public void testWriteTryLockWhenReadLockedFair() {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
        lock.readLock().lock();
        Thread t = new Thread(new Runnable() {

            public void run() {
                threadAssertFalse(lock.writeLock().tryLock());
            }
        });
        try {
            t.start();
            t.join();
            lock.readLock().unlock();
        } catch (Exception e) {
            unexpectedException();
        }
    }
