    void triggerGarbageCollection() {
        try {
            readWriteLock.writeLock().lock();
            timeSeriesCache.remove(this);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
