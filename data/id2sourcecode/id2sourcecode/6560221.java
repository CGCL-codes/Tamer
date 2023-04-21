    public void resetCacheKey(CacheKey key, Object object, Object writeLockValue, long readTime) {
        key.acquire();
        key.setObject(object);
        key.setWriteLockValue(writeLockValue);
        key.setReadTime(readTime);
        key.release();
    }
