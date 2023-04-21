    public void testWriterBlockingWriter() throws Exception {
        System.out.println("---> testWriterBlockingWriter");
        MROWLock lock = new MROWLock();
        assertNotLocked(lock);
        lock.acquireWrite();
        assertOnlyLocallyWriteLocked(lock);
        WriterThread writerThread = new WriterThread(lock);
        writerThread.start();
        System.out.println("Waiting for second writer to block.");
        while (!writerThread.mInAcquire) {
            assertTrue(!writerThread.mAcquired);
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
            if (writerThread.mException != null) {
                throw writerThread.mException;
            }
        }
        synchronized (lock) {
            assertTrue(writerThread.mInAcquire);
            assertTrue(!writerThread.mAcquired);
        }
        assertOnlyLocallyWriteLocked(lock);
        System.out.println("Second writer blocked. Releasing first writer...");
        lock.releaseWrite();
        System.out.println("Waiting for second writer to acquire...");
        while (!writerThread.mAcquired) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
            if (writerThread.mException != null) {
                throw writerThread.mException;
            }
        }
        assertOnlyExternallyWriteLocked(lock);
        System.out.println("Second writer acquired.");
        writerThread.mRelease = true;
        System.out.println("Waiting for second writer to finish.");
        writerThread.join();
        if (writerThread.mException != null) {
            throw writerThread.mException;
        }
        assertNotLocked(lock);
        System.out.println("Finished.");
    }
