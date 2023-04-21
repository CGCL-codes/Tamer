    public Object putInIdentityMap(Object object, Vector key, Object writeLockValue, long readTime) {
        ClassDescriptor descriptor = getSession().getDescriptor(object);
        return putInIdentityMap(object, key, writeLockValue, readTime, descriptor);
    }
