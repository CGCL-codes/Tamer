    public void returnExclusiveWriteLock(RDFStatsDataset ds) throws RDFStatsModelException {
        checkLock(ds, true);
        if (log.isDebugEnabled()) {
            String dsStr = (ds != null) ? ds.toString() : "all RDF sources";
            log.debug("Thread " + Thread.currentThread().getName() + " returned exclusive write lock for " + dsStr + ".");
        }
        unlock(ds);
        changedItems.remove(ds);
    }
