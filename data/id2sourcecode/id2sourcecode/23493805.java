    public BatchingJdbcTemplate getJdbcTemplate(final IBizDriver bizDriver, final ITransactionContext transactionContext) throws SQLException, XAwareException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        final IChannelKey key = bizDriver.getChannelSpecificationKey();
        BatchingJdbcTemplate batchingJdbcTemplate = null;
        if (transactionContext != null) {
            batchingJdbcTemplate = (BatchingJdbcTemplate) transactionContext.getTransactionalChannel(key);
            if (batchingJdbcTemplate != null) {
                return batchingJdbcTemplate;
            }
        }
        DataSource ds = this.dynamicDataSources.get(key);
        if (ds == null) {
            ds = (DataSource) bizDriver.createChannelObject();
            this.dynamicDataSources.put(key, ds);
        }
        ITransactionalChannel.Type transactionalChannelType = (XAwareConstants.BIZDRIVER_ATTR_SQL_JDBC.equals(bizDriver.getBizDriverType())) ? ITransactionalChannel.Type.LOCAL_JDBC : ITransactionalChannel.Type.DISTRIBUTED_JDBC;
        batchingJdbcTemplate = new BatchingJdbcTemplate(ds, transactionalChannelType);
        if (transactionContext != null) {
            transactionContext.setTransactionalChannel(key, batchingJdbcTemplate);
        }
        return batchingJdbcTemplate;
    }
