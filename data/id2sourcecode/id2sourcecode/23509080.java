    public AbstractTrafficShapingHandler(ObjectSizeEstimator objectSizeEstimator, Executor executor, long writeLimit, long readLimit) {
        super();
        init(objectSizeEstimator, executor, writeLimit, readLimit, DEFAULT_CHECK_INTERVAL);
    }
