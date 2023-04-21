    private void acquireWriteLock() throws InterruptedException {
        Thread thread = Thread.currentThread();
        if (writeLock != null || waiters != 0) {
            if (writingThread == thread) {
                if (logger != null) writeLock.printStackTrace(logger);
                throw new MultipleMutableSnapshotsException("This thread already holds a mutable snapshot - please release before acquiring a new one");
            }
            ++waiters;
            try {
                if (timeout != 0) {
                    wait(timeout);
                } else {
                    wait();
                }
            } finally {
                --waiters;
            }
            if (writeLock != null) {
                if (logger != null) writeLock.printStackTrace(logger);
                throw new TimeoutException(thread + " failed to acquire write lock after waiting " + timeout + " ms");
            }
        }
        writeLock = new Throwable("Current write lock was acquired by " + thread + " here:");
        writingThread = thread;
    }
