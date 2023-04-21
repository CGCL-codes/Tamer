    public void testAwaitUninterruptibly() {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        final Condition c = lock.writeLock().newCondition();
        UninterruptableThread thread = new UninterruptableThread(lock.writeLock(), c);
        try {
            thread.start();
            while (!thread.lockStarted) {
                Thread.sleep(100);
            }
            lock.writeLock().lock();
            try {
                thread.interrupt();
                thread.canAwake = true;
                c.signal();
            } finally {
                lock.writeLock().unlock();
            }
            thread.join();
            assertTrue(thread.interrupted);
            assertFalse(thread.isAlive());
        } catch (Exception ex) {
            unexpectedException();
        }
    }
