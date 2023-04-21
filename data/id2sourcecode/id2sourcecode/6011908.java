    public synchronized void writeUnlock() {
        if (activeWriters != 1 || lockCount <= 0) {
            throw new InternalError("Unbalanced writeLock()/writeUnlock() calls");
        }
        if (Thread.currentThread() != writerThread) {
            throw new InternalError("writeUnlock() from wrong thread");
        }
        if (--lockCount == 0) {
            --activeWriters;
            writerThread = null;
            notifyAll();
        }
    }
