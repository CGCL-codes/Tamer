    public AbstractTrafficShapingHandler(Executor executor, long writeLimit, long readLimit, long checkInterval) {
        super();
        init(new DefaultObjectSizeEstimator(), executor, writeLimit, readLimit, checkInterval);
    }
