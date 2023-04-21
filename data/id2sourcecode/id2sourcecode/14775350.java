    public synchronized void writeLock() {
        if (writerThread != null) {
            if (Thread.currentThread() == writerThread) {
                ++lockCount;
                return;
            }
        }
        if (allowWrite()) {
            claimWriteLock();
            return;
        }
        ++waitingWriters;
        while (!allowWrite()) {
            try {
                wait();
            } catch (InterruptedException e) {
                --waitingWriters;
                _log.error("Write Lock interrupted", e);
            }
        }
        --waitingWriters;
        claimWriteLock();
    }
