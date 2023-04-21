    public CacheKey internalPutInIdentityMap(Object object, Vector key, Object writeLockValue, long readTime, ClassDescriptor descriptor) {
        return getIdentityMapManager().putInIdentityMap(object, key, writeLockValue, readTime, descriptor);
    }
