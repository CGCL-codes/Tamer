    public synchronized void writeLock() {
        if (writerThread != null) {
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
                Log.log(Log.ERROR, this, e);
                return;
            }
        }
        --waitingWriters;
        claimWriteLock();
    }
