    public GlobalTrafficShapingHandler(ObjectSizeEstimator objectSizeEstimator, Executor executor, long writeLimit, long readLimit) {
        super(objectSizeEstimator, executor, writeLimit, readLimit);
        createGlobalTrafficCounter();
    }
