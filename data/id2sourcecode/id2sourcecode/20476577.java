    public synchronized void removeObject(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        ObjectPoolEntry entry = (ObjectPoolEntry) objectEntryMap.get(o);
        if (entry == null) {
            throw new IllegalArgumentException("Object not from this pool");
        }
        if (entry.dirty) {
            debug.write("object already marked for removal... releasing...");
            releaseEntry(entry);
            return;
        }
        removeAll();
        releaseEntry(entry);
    }
