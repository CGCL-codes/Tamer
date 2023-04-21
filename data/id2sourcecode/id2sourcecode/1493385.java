    public synchronized void unlockWrite() {
        if (writeLocks_ > 0) {
            assertTrue("current thread does not own the write lock", writer_ == Thread.currentThread());
            assertTrue("writeLock has already been unlocked", writeLocks_ > 0);
            writeLocks_--;
            if (writeLocks_ == 0) {
                writer_ = null;
            }
        }
        FuLog.trace("CSG : unlock write:" + Thread.currentThread().getName());
        notifyAll();
    }
