    @Deprecated
    public ICacheEntry<K, V> allocateEntry(final K key, final Lock readLock, final Lock writeLock) {
        ensureNotNull(key);
        readLock.lock();
        ICacheEntry<K, V> entry = _delegate.get(key);
        readLock.unlock();
        writeLock.lock();
        if (entry != null && entry.pin()) {
            _policy.recordAccess(entry);
        } else {
            entry = _policy.allocateEntry(_delegate, key, null);
        }
        writeLock.unlock();
        return entry;
    }
