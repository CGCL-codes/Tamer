    public SyncSortedMap(SortedMap map, ReadWriteLock rwl) {
        super(map, rwl.readLock(), rwl.writeLock());
    }
