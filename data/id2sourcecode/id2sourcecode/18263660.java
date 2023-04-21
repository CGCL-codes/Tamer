    public void testWriteTryLock_Timeout() {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        lock.writeLock().lock();
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    threadAssertFalse(lock.writeLock().tryLock(1, TimeUnit.MILLISECONDS));
                } catch (Exception ex) {
                    threadUnexpectedException();
                }
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
