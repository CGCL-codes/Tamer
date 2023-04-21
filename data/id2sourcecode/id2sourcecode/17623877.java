    public boolean executeRepositoryUpdateTransaction(DatabaseRepositoryQuery[] queries) {
        Connection connection = null;
        int pos = 0;
        NamedParameterStatement[] queriesArray;
        boolean autoCommit = true;
        if (queries.length == 0) {
            return true;
        }
        try {
            connection = this.dataSource.getConnection();
            queriesArray = this.getRepositoryPreparedStatements(connection, queries);
            autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            for (pos = 0; pos < queriesArray.length; pos++) {
                try {
                    queriesArray[pos].executeUpdate();
                } finally {
                    queriesArray[pos].close();
                }
            }
            if (autoCommit) {
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (SQLException oException) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException oRollbackException) {
                throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, oRollbackException);
            }
        } finally {
            close(connection);
        }
        return true;
    }
