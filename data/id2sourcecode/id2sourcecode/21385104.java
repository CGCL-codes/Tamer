    public GlobalTrafficShapingHandler(Executor executor, long writeLimit, long readLimit) {
        super(executor, writeLimit, readLimit);
        createGlobalTrafficCounter();
    }
