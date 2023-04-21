    @Override
    <T> T synchronizeWriteThenReadImpl(Object mutex, Callback<?> write_callback, Callback<T> read_callback, Callback<?> release_callback) {
        if (mutex instanceof ReentrantReadWriteLock) {
            ReentrantReadWriteLock lock = (ReentrantReadWriteLock) mutex;
            boolean read_lock = false;
            boolean write_lock = false;
            if (lock.getReadHoldCount() > 0 && lock.getWriteHoldCount() == 0) throw new IllegalStateException("Lock cannot be upgraded from read to write");
            lock.writeLock().lock();
            write_lock = true;
            lock.readLock().lock();
            read_lock = true;
            try {
                write_callback.action();
                lock.writeLock().unlock();
                write_lock = false;
                return read_callback.action();
            } finally {
                if (write_lock) {
                    try {
                        if (!read_lock) {
                            if (lock.getReadHoldCount() == 0 && lock.getWriteHoldCount() == 1 && release_callback != null) release_callback.action();
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
                if (read_lock) {
                    try {
                        if (lock.getReadHoldCount() == 1 && lock.getWriteHoldCount() == 0 && release_callback != null) release_callback.action();
                    } finally {
                        lock.readLock().unlock();
                    }
                }
            }
        } else if (mutex instanceof SyncList) {
            throw new UnsupportedOperationException("SyncList cannot be locked for write then read.");
        } else return super.synchronizeWriteThenReadImpl(mutex, write_callback, read_callback, release_callback);
    }
