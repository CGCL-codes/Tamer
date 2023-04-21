    boolean set(final StoredObject object) {
        boolean result = false;
        Lock l = readWriteLock.writeLock();
        l.lock();
        try {
            if (get(object.getUuid()) == null) {
                objects.add(object);
            }
            result = true;
        } finally {
            l.unlock();
        }
        return result;
    }
