    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        return new ReferenceCacheKey(primaryKey, object, writeLockValue, readTime);
    }
