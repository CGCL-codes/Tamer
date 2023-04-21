    public synchronized void lockWrite() throws InterruptedException {
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
                throw e;
            }
        }
        --waitingWriters;
        claimWriteLock();
    }
