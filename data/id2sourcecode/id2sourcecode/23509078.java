    public AbstractTrafficShapingHandler(ObjectSizeEstimator objectSizeEstimator, Executor executor, long writeLimit, long readLimit, long checkInterval) {
        super();
        init(objectSizeEstimator, executor, writeLimit, readLimit, checkInterval);
    }
