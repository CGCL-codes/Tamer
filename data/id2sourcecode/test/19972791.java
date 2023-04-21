    public void requestExclusiveWriteLock(RDFStatsDataset ds) {
        String dsStr = (ds != null) ? ds.toString() : "all RDF sources";
        while (lockOwner(ds) != null) {
            try {
                if (log.isInfoEnabled()) log.info("Waiting for the exclusive write lock for " + dsStr + "...");
                wait(WAIT_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
        if (log.isDebugEnabled()) log.debug("Thread " + Thread.currentThread().getName() + " obtained exclusive write lock for " + dsStr + ".");
        lock(ds);
        resetChangedItems(ds);
    }
