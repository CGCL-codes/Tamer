    public CacheKey put(Vector primaryKey, Object object, Object writeLockValue, long readTime) {
        CacheKey cacheKey = getCacheKey(primaryKey);
        if (cacheKey != null) {
            resetCacheKey(cacheKey, object, writeLockValue);
            put(cacheKey);
        } else {
            cacheKey = createCacheKey(primaryKey, object, writeLockValue, readTime);
            put(cacheKey);
        }
        return cacheKey;
    }
