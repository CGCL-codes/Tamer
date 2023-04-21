    public void queueHeaderRewrite() {
        try {
            readWriteLock.writeLock().lock();
            writeBehindCache.scheduleFlushCacheTask(appendPeriod.getLengthInMillis());
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
