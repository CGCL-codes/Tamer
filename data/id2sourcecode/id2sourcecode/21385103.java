    public GlobalTrafficShapingHandler(Executor executor, long writeLimit, long readLimit, long checkInterval) {
        super(executor, writeLimit, readLimit, checkInterval);
        createGlobalTrafficCounter();
    }
