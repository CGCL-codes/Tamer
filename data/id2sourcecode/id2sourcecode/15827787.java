    private static synchronized ExecutorService getSingletonExecutorService() {
        if (singletonExecutorService == null) {
            singletonExecutorService = Executors.newCachedThreadPool(new ThreadFactory() {

                private final AtomicInteger threadCount = new AtomicInteger();

                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("ektorp-doc-writer-thread-%s", threadCount.incrementAndGet()));
                }
            });
        }
        return singletonExecutorService;
    }
