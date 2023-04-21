    public final void beginWriteNonReentrant() {
        final Thread thread = Thread.currentThread();
        if (thread == getExclusiveOwnerThread()) throw new IllegalMonitorStateException("current thread already holds write lock");
        if (compareAndSetState(0, 1)) setExclusiveOwnerThread(thread); else acquire(1);
    }
