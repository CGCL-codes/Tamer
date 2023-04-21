    public CacheKey internalPutInIdentityMap(Object domainObject, Vector key, Object writeLockValue, long readTime, ClassDescriptor descriptor) {
        if (descriptor.isIsolated()) {
            return getIdentityMapManager().putInIdentityMap(domainObject, key, writeLockValue, readTime, descriptor);
        } else {
            return ((IsolatedClientSession) session).getParent().getIdentityMapAccessorInstance().internalPutInIdentityMap(domainObject, key, writeLockValue, readTime, descriptor);
        }
    }
