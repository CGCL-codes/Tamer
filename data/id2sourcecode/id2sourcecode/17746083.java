    private static void init() {
        cache = new ConcurrentLRUCache<String, String>(1000);
        readExec = Executors.newCachedThreadPool();
        writeExec = Executors.newCachedThreadPool();
        totalReadTime = new AtomicLong();
        totalWriteTime = new AtomicLong();
        totalReadCount = new AtomicLong();
        totalWriteCount = new AtomicLong();
        timePerReadThread = new CopyOnWriteArrayList<Long>();
        timePerWriteThread = new CopyOnWriteArrayList<Long>();
    }
