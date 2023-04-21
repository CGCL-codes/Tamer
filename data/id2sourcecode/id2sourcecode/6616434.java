    public synchronized Snapshot getSnapshot() {
        return new Snapshot(_totalOps, _connectionWaitTime, _cachedQueries, _uncachedQueries, _explicitQueries, _cachedRecords, _uncachedRecords, _readHisto.clone(), _readTime, _writeHisto.clone(), _writeTime);
    }
