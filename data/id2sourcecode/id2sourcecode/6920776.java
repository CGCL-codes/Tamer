    public void writerWait(Object notifier, long time) {
        try {
            synchronized (notifier) {
                writeUnlock();
                notifier.wait(time);
            }
        } catch (InterruptedException e) {
            throw new ConcurrentLockException("write wait interrupted in thread");
        } finally {
            writeLock();
        }
    }
