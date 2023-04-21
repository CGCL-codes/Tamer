    public boolean afterUpdate(Object key, Object value, Object version, SoftLock lock) throws CacheException {
        log.error("Application attempted to edit read only item: " + key);
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }
