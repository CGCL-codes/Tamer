    public SyncMap(Map map, Sync readLock, Sync writeLock) {
        c_ = map;
        rd_ = readLock;
        wr_ = writeLock;
    }
