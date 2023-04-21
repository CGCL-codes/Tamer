    public GlobalTrafficShapingHandler(ObjectSizeEstimator objectSizeEstimator, Executor executor, long writeLimit, long readLimit, long checkInterval) {
        super(objectSizeEstimator, executor, writeLimit, readLimit, checkInterval);
        createGlobalTrafficCounter();
    }
