    public synchronized void exitRead() {
        if (writeLockowner == Thread.currentThread()) return;
        if (--status == 0) notifyAll();
    }
