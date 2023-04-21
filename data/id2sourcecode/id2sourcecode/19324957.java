    public void persistOperations(TransactionTemplate template, OnmsDao<?, ?> dao) {
        m_stats.beginProcessingOps();
        m_stats.setDeleteCount(getDeleteCount());
        m_stats.setInsertCount(getInsertCount());
        m_stats.setUpdateCount(getUpdateCount());
        ExecutorService dbPool = Executors.newFixedThreadPool(m_writeThreads);
        preprocessOperations(template, dao, new OperationIterator(), dbPool);
        shutdownAndWaitForCompletion(dbPool, "persister interrupted!");
        m_stats.finishProcessingOps();
    }
