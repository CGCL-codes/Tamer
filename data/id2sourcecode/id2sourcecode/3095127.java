    @Override
    <T> T synchronizeConditionalWriteThenReadImpl(Object mutex, Condition write_condition, Callback<?> write_callback, Callback<T> read_callback, Callback<?> release_callback) {
        if (mutex instanceof ReentrantReadWriteLock) {
            ReentrantReadWriteLock lock = (ReentrantReadWriteLock) mutex;
            boolean read_lock = false;
            boolean write_lock = false;
            int read_hold_count = lock.getReadHoldCount();
            if (read_hold_count > 0 && write_condition.isTrue(read_hold_count)) throw new IllegalStateException("Lock cannot be upgraded from read to write");
            lock.readLock().lock();
            read_lock = true;
            try {
                if (write_condition.isTrue(read_hold_count)) {
                    lock.readLock().unlock();
                    read_lock = false;
                    lock.writeLock().lock();
                    write_lock = true;
                    lock.readLock().lock();
                    read_lock = true;
                    if (write_condition.isTrue(read_hold_count)) {
                        write_callback.action();
                        lock.writeLock().unlock();
                        write_lock = false;
                    }
                }
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
            throw new UnsupportedOperationException("SyncList cannot be locked for conditional write then read.");
        } else return super.synchronizeConditionalWriteThenReadImpl(mutex, write_condition, write_callback, read_callback, release_callback);
    }
