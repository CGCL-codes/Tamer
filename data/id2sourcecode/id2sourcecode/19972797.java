    private void checkLock(RDFStatsDataset ds, boolean warnOnly) throws RDFStatsModelException {
        Thread prev = lockOwner(ds);
        String dsStr = (ds == null) ? "all statistics" : ds.toString();
        if (prev == null) {
            String msg = "Unauthorized modification operation: Thread " + Thread.currentThread().getName() + " has no exclusive write lock for " + dsStr + ".";
            if (warnOnly) log.warn(msg); else throw new RDFStatsModelException(msg);
        }
        if (Thread.currentThread() != prev) {
            String msg = "Unauthorized modification operation: Thread " + Thread.currentThread().getName() + " has no exclusive write lock for " + dsStr + " (it is locked by Thread " + prev.getId() + ".";
            if (warnOnly) log.warn(msg); else throw new RDFStatsModelException(msg);
        }
    }
