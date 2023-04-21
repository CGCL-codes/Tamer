    public synchronized void releaseWriteLock() {
        if (Thread.currentThread() == _writerThread) {
            if (--_writeCount <= 0) {
                _writerThread = null;
                notifyAll();
            }
        }
    }
