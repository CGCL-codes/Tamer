    public BatchCoordinator(Set<Class<?>> rootEntities, SearchFactoryImplementor searchFactoryImplementor, SessionFactory sessionFactory, int objectLoadingThreads, int collectionLoadingThreads, CacheMode cacheMode, int objectLoadingBatchSize, long objectsLimit, boolean optimizeAtEnd, boolean purgeAtStart, boolean optimizeAfterPurge, MassIndexerProgressMonitor monitor, Integer writerThreads) {
        this.rootEntities = rootEntities.toArray(new Class<?>[rootEntities.size()]);
        this.searchFactoryImplementor = searchFactoryImplementor;
        this.sessionFactory = sessionFactory;
        this.objectLoadingThreads = objectLoadingThreads;
        this.collectionLoadingThreads = collectionLoadingThreads;
        this.cacheMode = cacheMode;
        this.objectLoadingBatchSize = objectLoadingBatchSize;
        this.optimizeAtEnd = optimizeAtEnd;
        this.purgeAtStart = purgeAtStart;
        this.optimizeAfterPurge = optimizeAfterPurge;
        this.monitor = monitor;
        this.objectsLimit = objectsLimit;
        this.writerThreads = writerThreads;
        this.endAllSignal = new CountDownLatch(rootEntities.size());
    }
