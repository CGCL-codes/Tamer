    @Override
    public Statement createStatement(DbConnection connection) throws SqlException {
        try {
            PGSimpleDataSource source = new PGSimpleDataSource();
            source.setServerName(connection.hostname);
            source.setDatabaseName(connection.database);
            source.setPortNumber(connection.port);
            source.setUser(connection.username);
            source.setPassword(connection.pass);
            source.setSsl(connection.secure);
            GenericObjectPool connectionPool = new GenericObjectPool(null);
            ConnectionFactory connectionFactory = new DataSourceConnectionFactory(source);
            new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
            PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(!connection.transactive);
            return conn.createStatement();
        } catch (SQLException e) {
            try {
                throw new SqlException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.DatabaseConnectionError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new SqlException(null, name(new Locale("en", "US")), ErrorCode.DatabaseConnectionError.getCode());
            }
        }
    }
