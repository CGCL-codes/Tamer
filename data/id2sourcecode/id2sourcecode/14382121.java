    public void commit() throws IOException, ClassNotFoundException {
        if (logger.isLoggable(Level.FINE)) logger.fine("committing transaction " + this);
        setStatus(STATUS_COMMITTING);
        Storage storage = getStorageFactory().createStorage(committingStorageName());
        storage.close();
        if (!getGammaStore().isReadOnly()) {
            if (getContainerNames() != null) {
                getGammaStore().updateContainerNames(getContainerNames());
            }
            for (Iterator i = lockingInfosIterator(); i.hasNext(); ) {
                LockingInfo lockingInfo = (LockingInfo) i.next();
                if (logger.isLoggable(Level.FINER)) logger.finer("committing " + (lockingInfo.getType() == LockingInfo.LOCK_READ ? "read: " : " write: ") + lockingInfo.getObjectID());
                if (lockingInfo.getType() == LockingInfo.LOCK_READ) {
                    GammaContainer container = (GammaContainer) getGammaStore().containerForID(this, lockingInfo.getObjectID());
                    if (container.getGammaLock().level(null) == Lock.LEVEL_READ) {
                        container.getGammaLock().release(this);
                    }
                } else {
                    GammaContainer container = getGammaStore().commit(lockingInfo.getObjectID(), lockingInfo.getContainerLocation(), lockingInfo.getWipeOnCommit());
                    if (container != null && container.getGammaLock().isAcquiredBy(this)) {
                        container.getGammaLock().release(this);
                    }
                }
            }
        } else {
            for (Iterator i = lockingInfosIterator(); i.hasNext(); ) {
                LockingInfo lockingInfo = (LockingInfo) i.next();
                GammaContainer container = (GammaContainer) getGammaStore().containerForID(this, lockingInfo.getObjectID());
                if (container.getGammaLock().level(this) > Lock.LEVEL_NONE) {
                    container.lock().release(this);
                }
            }
        }
        deleteLogStorage();
        deleteCommittingStorage();
        deleteLockingInfoStorage();
        setStatus(STATUS_COMMITTED);
        if (logger.isLoggable(Level.FINE)) logger.fine("committed transaction " + this);
    }
