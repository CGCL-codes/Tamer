    public SyncMap(Map map, ReadWriteLock rwl) {
        this(map, rwl.readLock(), rwl.writeLock());
    }
