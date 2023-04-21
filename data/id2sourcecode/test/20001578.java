    public CachingModePage(final boolean parametersSaveable, final boolean initiatorControl, final boolean abortPrefetch, final boolean cachingAnalysisPermitted, final boolean discontinuity, final boolean sizeEnable, final boolean writebackCacheEnable, final boolean multiplicationFactor, final boolean readCacheDisable, final int demandReadRetentionPriority, final int writeRetentionPriority, final int disablePrefetchTransferLength, final int minimumPrefetch, final int maximumPrefetch, final int maximumPrefetchCeiling, final boolean forceSequentialWrite, final boolean logicalBlockCacheSegmentSize, final boolean disableReadAhead, final boolean nonVolatileCacheDisabled, final int numberOfCacheSegments, final int cacheSegmentSize) {
        super(parametersSaveable, 0x08, 0x12);
        this.initiatorControl = initiatorControl;
        this.abortPrefetch = abortPrefetch;
        this.cachingAnalysisPermitted = cachingAnalysisPermitted;
        this.discontinuity = discontinuity;
        this.sizeEnable = sizeEnable;
        this.writebackCacheEnable = writebackCacheEnable;
        this.multiplicationFactor = multiplicationFactor;
        this.readCacheDisable = readCacheDisable;
        this.demandReadRetentionPriority = (byte) (demandReadRetentionPriority & 0xf);
        this.writeRetentionPriority = (byte) (writeRetentionPriority & 0xf);
        this.disablePrefetchTransferLength = disablePrefetchTransferLength & 0xffff;
        this.minimumPrefetch = minimumPrefetch & 0xffff;
        this.maximumPrefetch = maximumPrefetch & 0xffff;
        this.maximumPrefetchCeiling = maximumPrefetchCeiling & 0xffff;
        this.forceSequentialWrite = forceSequentialWrite;
        this.logicalBlockCacheSegmentSize = logicalBlockCacheSegmentSize;
        this.disableReadAhead = disableReadAhead;
        this.nonVolatileCacheDisabled = nonVolatileCacheDisabled;
        this.numberOfCacheSegments = (short) (numberOfCacheSegments & 0xff);
        this.cacheSegmentSize = cacheSegmentSize & 0xffff;
    }
