    @Deprecated
    public ICacheEntry<K, V> allocateEntryForClock(final K key, final Lock readLock, final Lock writeLock) {
        ensureNotNull(key);
        readLock.lock();
        ICacheEntry<K, V> entry = _delegate.get(key);
        readLock.unlock();
        if (entry != null && entry.pin()) {
            _policy.recordAccess(entry);
        } else {
            writeLock.lock();
            entry = _policy.allocateEntry(_delegate, key, null);
            writeLock.unlock();
        }
        return entry;
    }
