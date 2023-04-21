    public void runTest(Directory directory) throws Exception {
        TimedThread[] threads = new TimedThread[4];
        IndexWriter writer = new MockIndexWriter(directory, ANALYZER, true, IndexWriter.MaxFieldLength.UNLIMITED);
        writer.setMaxBufferedDocs(7);
        writer.setMergeFactor(3);
        for (int i = 0; i < 100; i++) {
            Document d = new Document();
            d.add(new Field("id", Integer.toString(i), Field.Store.YES, Field.Index.NOT_ANALYZED));
            d.add(new Field("contents", English.intToEnglish(i), Field.Store.NO, Field.Index.ANALYZED));
            if ((i - 1) % 7 == 0) {
                writer.commit();
            }
            writer.addDocument(d);
        }
        writer.commit();
        IndexReader r = IndexReader.open(directory, true);
        assertEquals(100, r.numDocs());
        r.close();
        IndexerThread indexerThread = new IndexerThread(writer, threads);
        threads[0] = indexerThread;
        indexerThread.start();
        IndexerThread indexerThread2 = new IndexerThread(writer, threads);
        threads[1] = indexerThread2;
        indexerThread2.start();
        SearcherThread searcherThread1 = new SearcherThread(directory, threads);
        threads[2] = searcherThread1;
        searcherThread1.start();
        SearcherThread searcherThread2 = new SearcherThread(directory, threads);
        threads[3] = searcherThread2;
        searcherThread2.start();
        indexerThread.join();
        indexerThread2.join();
        searcherThread1.join();
        searcherThread2.join();
        writer.close();
        assertTrue("hit unexpected exception in indexer", !indexerThread.failed);
        assertTrue("hit unexpected exception in indexer2", !indexerThread2.failed);
        assertTrue("hit unexpected exception in search1", !searcherThread1.failed);
        assertTrue("hit unexpected exception in search2", !searcherThread2.failed);
    }
