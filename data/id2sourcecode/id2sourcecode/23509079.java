    public AbstractTrafficShapingHandler(Executor executor, long writeLimit, long readLimit) {
        super();
        init(new DefaultObjectSizeEstimator(), executor, writeLimit, readLimit, DEFAULT_CHECK_INTERVAL);
    }
