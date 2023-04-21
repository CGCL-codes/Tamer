    public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) {
        log.error("Application attempted to edit read only item: " + key);
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }
