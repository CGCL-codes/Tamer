    public void testRandomExceptions() throws Throwable {
        MockRAMDirectory dir = new MockRAMDirectory();
        MockIndexWriter writer = new MockIndexWriter(dir, new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        ((ConcurrentMergeScheduler) writer.getMergeScheduler()).setSuppressExceptions();
        writer.setRAMBufferSizeMB(0.1);
        if (DEBUG) writer.setInfoStream(System.out);
        IndexerThread thread = new IndexerThread(0, writer);
        thread.run();
        if (thread.failure != null) {
            thread.failure.printStackTrace(System.out);
            fail("thread " + thread.getName() + ": hit unexpected failure");
        }
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
