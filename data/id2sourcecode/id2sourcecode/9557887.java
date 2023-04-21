    public synchronized void releaseWrite() {
        Thread thread = Thread.currentThread();
        if (mWriteLockOwner != thread) {
            throw new RuntimeException("Attempted to release a write lock when one is not held by this thread.");
        }
        --mNumWriteLockReentries;
        assert mNumWriteLockReentries >= 0;
        if (mNumWriteLockReentries == 0) {
            mWriteLockOwner = null;
        }
        notifyAll();
    }
