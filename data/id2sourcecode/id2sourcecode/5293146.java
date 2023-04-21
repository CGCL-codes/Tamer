    public Object putInIdentityMap(Object object, Vector key, Object writeLockValue, long readTime, ClassDescriptor descriptor) {
        CacheKey cacheKey = internalPutInIdentityMap(object, key, writeLockValue, readTime, descriptor);
        if (cacheKey == null) {
            return null;
        }
        return cacheKey.getObject();
    }
