    protected BatchCoordinator createCoordinator() {
        return new BatchCoordinator(rootEntities, searchFactoryImplementor, sessionFactory, objectLoadingThreads, collectionLoadingThreads, cacheMode, objectLoadingBatchSize, objectsLimit, optimizeAtEnd, purgeAtStart, optimizeAfterPurge, monitor, writerThreads);
    }
