    public SoftLock lock(Object key, Object version) {
        log.error("Application attempted to edit read only item: " + key);
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }
