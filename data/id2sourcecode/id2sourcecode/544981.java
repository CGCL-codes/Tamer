    public void testWriteTryLockWhenLocked() {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.writeLock().lock();
        Thread t = new Thread(new Runnable() {

            public void run() {
                threadAssertFalse(lock.writeLock().tryLock());
            }
        });
        try {
            t.start();
            t.join();
            lock.writeLock().unlock();
        } catch (Exception e) {
            unexpectedException();
        }
    }
