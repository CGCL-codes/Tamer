    public void testIndexing() throws Exception {
        Directory mainDir = new MockRAMDirectory();
        IndexWriter writer = new IndexWriter(mainDir, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.LIMITED);
        writer.setUseCompoundFile(false);
        IndexReader reader = writer.getReader();
        reader.close();
        writer.setMergeFactor(2);
        writer.setMaxBufferedDocs(10);
        RunThread[] indexThreads = new RunThread[4];
        for (int x = 0; x < indexThreads.length; x++) {
            indexThreads[x] = new RunThread(x % 2, writer);
            indexThreads[x].setName("Thread " + x);
            indexThreads[x].start();
        }
        long startTime = System.currentTimeMillis();
        long duration = 5 * 1000;
        while ((System.currentTimeMillis() - startTime) < duration) {
            Thread.sleep(100);
        }
        int delCount = 0;
        int addCount = 0;
        for (int x = 0; x < indexThreads.length; x++) {
            indexThreads[x].run = false;
            assertTrue(indexThreads[x].ex == null);
            addCount += indexThreads[x].addCount;
            delCount += indexThreads[x].delCount;
        }
        for (int x = 0; x < indexThreads.length; x++) {
            indexThreads[x].join();
        }
        writer.close();
        mainDir.close();
    }
