    public CacheKey createCacheKey(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        return new LinkedCacheKey(primaryKey, object, writeLockValue, readTime);
    }
