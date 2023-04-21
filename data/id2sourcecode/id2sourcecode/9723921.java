    private void createDataSources(String driverName, String uri, String dbUserName, String dbPassword, int minCon, int maxConn) {
        logger.info("initializing database connection manager");
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.error("unable to load DB driver, game over");
            return;
        }
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(uri, dbUserName, dbPassword);
        GenericObjectPool readwriteConnectionPool = new GenericObjectPool(null);
        readwriteConnectionPool.setMinIdle(minCon);
        readwriteConnectionPool.setMaxActive(maxConn);
        @SuppressWarnings("unused") PoolableConnectionFactory readwritePoolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, readwriteConnectionPool, null, null, false, true);
        PoolingDataSource readwriteDataSource = new PoolingDataSource(readwriteConnectionPool);
        mDataSource = readwriteDataSource;
    }
