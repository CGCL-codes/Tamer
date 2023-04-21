    void delete(final StoredObject object) {
        Lock l = readWriteLock.writeLock();
        l.lock();
        try {
            objects.remove(object);
        } finally {
            l.unlock();
        }
    }
