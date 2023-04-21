    public void testMultipleReadersBlockingWriter() throws Exception {
        System.out.println("---> testMultipleReadersBlockingWriter");
        MROWLock lock = new MROWLock();
        assertNotLocked(lock);
        ReaderThread[] readerThreads = acquireReadLocks(lock);
        assertOnlyExternallyReadLocked(lock);
        WriterThread writerThread = new WriterThread(lock);
        writerThread.start();
        System.out.println("Waiting for writer to block.");
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
        assertOnlyExternallyReadLocked(lock);
        System.out.println("Writer blocked. Telling readers to release...");
        releaseReadLocks(readerThreads);
        System.out.println("Waiting for writer to acquire...");
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
        System.out.println("Write acquired.");
        writerThread.mRelease = true;
        System.out.println("Waiting for writer to finish.");
        writerThread.join();
        if (writerThread.mException != null) {
            throw writerThread.mException;
        }
        assertNotLocked(lock);
        System.out.println("Finished.");
    }
