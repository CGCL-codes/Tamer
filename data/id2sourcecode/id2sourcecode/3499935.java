    public static void main(String[] args) throws Exception {
        IndexWriter writer = new IndexWriter("index", ANALYZER, true);
        Thread indexerThread = new IndexerThread(writer);
        indexerThread.start();
        Thread.sleep(1000);
        SearcherThread searcherThread1 = new SearcherThread(false);
        searcherThread1.start();
        SEARCHER = new IndexSearcher("index");
        SearcherThread searcherThread2 = new SearcherThread(true);
        searcherThread2.start();
        SearcherThread searcherThread3 = new SearcherThread(true);
        searcherThread3.start();
    }
