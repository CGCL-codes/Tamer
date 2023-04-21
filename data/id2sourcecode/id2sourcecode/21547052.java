    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        return new CacheKey(primaryKey, object, writeLockValue, readTime);
    }
