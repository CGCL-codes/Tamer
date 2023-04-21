    public void testRandomExceptionsThreads() throws Throwable {
        MockRAMDirectory dir = new MockRAMDirectory();
        MockIndexWriter writer = new MockIndexWriter(dir, new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        ((ConcurrentMergeScheduler) writer.getMergeScheduler()).setSuppressExceptions();
        writer.setRAMBufferSizeMB(0.2);
        if (DEBUG) writer.setInfoStream(System.out);
        final int NUM_THREADS = 4;
        final IndexerThread[] threads = new IndexerThread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new IndexerThread(i, writer);
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) threads[i].join();
        for (int i = 0; i < NUM_THREADS; i++) if (threads[i].failure != null) fail("thread " + threads[i].getName() + ": hit unexpected failure");
        writer.commit();
        try {
            writer.close();
        } catch (Throwable t) {
            System.out.println("exception during close:");
            t.printStackTrace(System.out);
            writer.rollback();
        }
        IndexReader r2 = IndexReader.open(dir, true);
        final int count = r2.docFreq(new Term("content4", "aaa"));
        final int count2 = r2.docFreq(new Term("content4", "ddd"));
        assertEquals(count, count2);
        r2.close();
        _TestUtil.checkIndex(dir);
    }
