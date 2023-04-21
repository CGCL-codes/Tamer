    public void _testStressLocks(LockFactory lockFactory, File indexDir) throws Exception {
        FSDirectory fs1 = FSDirectory.open(indexDir, lockFactory);
        IndexWriter w = new IndexWriter(fs1, new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
        addDoc(w);
        w.close();
        WriterThread writer = new WriterThread(100, fs1);
        SearcherThread searcher = new SearcherThread(100, fs1);
        writer.start();
        searcher.start();
        while (writer.isAlive() || searcher.isAlive()) {
            Thread.sleep(1000);
        }
        assertTrue("IndexWriter hit unexpected exceptions", !writer.hitException);
        assertTrue("IndexSearcher hit unexpected exceptions", !searcher.hitException);
        _TestUtil.rmDir(indexDir);
    }
