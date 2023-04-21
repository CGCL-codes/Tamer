    public void readIdentityInformation(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
        cacheKey = (CacheKey) stream.readObject();
        className = (String) stream.readObject();
        writeLockValue = stream.readObject();
    }
