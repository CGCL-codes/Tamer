    public StockHistoryMonitor(int nThreads, int databaseSize) {
        pool = Executors.newFixedThreadPool(nThreads);
        readWriteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        readerLock = readWriteLock.readLock();
        writerLock = readWriteLock.writeLock();
        this.DATABASE_SIZE = databaseSize;
        this.stockHistorySerializer = null;
    }
