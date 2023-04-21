    @Override
    <T> T synchronizeWriteImpl(Object mutex, Callback<T> callback, Callback<?> release_callback) {
        if (mutex instanceof ReentrantReadWriteLock) {
            ReentrantReadWriteLock lock = (ReentrantReadWriteLock) mutex;
            if (lock.getReadHoldCount() > 0 && lock.getWriteHoldCount() == 0) throw new IllegalStateException("Lock cannot be upgraded from read to write");
            lock.writeLock().lock();
            try {
                return callback.action();
            } finally {
                try {
                    if (lock.getReadHoldCount() == 0 && lock.getWriteHoldCount() == 1 && release_callback != null) release_callback.action();
                } finally {
                    lock.writeLock().unlock();
                }
            }
        } else if (mutex instanceof SyncList) {
            if (release_callback != null) throw new QueujException("release_callback not supported for SyncLists");
            SyncList sync_list = (SyncList) mutex;
            try {
                sync_list.lockAll();
                return callback.action();
            } finally {
                sync_list.unlockAll();
            }
        } else return super.synchronizeWriteImpl(mutex, callback, release_callback);
    }
