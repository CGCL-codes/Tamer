    private void claimWriteLock() {
        ++activeWriters;
        Debug.assertTrue(writerThread == null);
        writerThread = Thread.currentThread();
        Debug.assertTrue(lockCount == 0);
        lockCount = 1;
    }
