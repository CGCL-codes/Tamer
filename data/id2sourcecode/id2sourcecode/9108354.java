    public synchronized void unlockWrite() {
        Debug.assertTrue(activeWriters == 1);
        Debug.assertTrue(lockCount > 0);
        Debug.assertTrue(Thread.currentThread() == writerThread);
        if (--lockCount == 0) {
            --activeWriters;
            writerThread = null;
            notifyAll();
        }
    }
