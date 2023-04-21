    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        return new WeakCacheKey(primaryKey, object, writeLockValue, readTime);
    }
