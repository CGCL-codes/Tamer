    private void dropAndCreateTables(boolean create) {
        DataSourceGroupEntry group;
        DataSourceEntry entry;
        int j;
        XAConnection xaConnection;
        Connection connection;
        Statement statement;
        ArrayList visited;
        TransactionManager transactionManager;
        try {
            visited = new ArrayList();
            xaConnection = null;
            connection = null;
            statement = null;
            transactionManager = _txDomain.getTransactionManager();
            try {
                transactionManager.rollback();
            } catch (Exception e) {
            }
            for (int i = _groups.size(); --i >= 0; ) {
                group = (DataSourceGroupEntry) _groups.get(i);
                for (j = group.getNumberOfDataSourceEntries(); --j >= 0; ) {
                    entry = group.getDataSourceEntry(j);
                    if (!visited.contains(entry)) {
                        visited.add(entry);
                        try {
                            if (!entry.getCreateDropTables()) {
                                xaConnection = entry.getXAConnection();
                                connection = xaConnection.getConnection();
                                transactionManager.begin();
                                transactionManager.getTransaction().enlistResource(xaConnection.getXAResource());
                                statement = connection.createStatement();
                                try {
                                    statement.executeUpdate("delete from " + entry.getTableName());
                                } catch (SQLException e) {
                                    if (create) {
                                        throw e;
                                    }
                                }
                            } else {
                                xaConnection = entry.getXAConnectionForCreation();
                                connection = xaConnection.getConnection();
                                statement = connection.createStatement();
                                transactionManager.begin();
                                transactionManager.getTransaction().enlistResource(xaConnection.getXAResource());
                                try {
                                    statement.execute("drop table " + entry.getTableName());
                                } catch (SQLException e) {
                                }
                                if (create) {
                                    try {
                                        statement.execute("create table " + entry.getTableName() + " (" + PRIMARY_KEY_COLUMN_NAME + " varchar(255) primary key, " + VALUE_COLUMN_NAME + " varchar(255))");
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                        transactionManager.rollback();
                                        if (null != statement) {
                                            try {
                                                statement.close();
                                            } catch (SQLException e1) {
                                            }
                                            statement = null;
                                        }
                                        if (null != connection) {
                                            try {
                                                connection.close();
                                            } catch (SQLException e1) {
                                            }
                                            connection = null;
                                        }
                                        if (null != xaConnection) {
                                            try {
                                                xaConnection.close();
                                            } catch (SQLException e1) {
                                            }
                                            xaConnection = null;
                                        }
                                        xaConnection = entry.getXAConnection();
                                        connection = xaConnection.getConnection();
                                        transactionManager.begin();
                                        transactionManager.getTransaction().enlistResource(xaConnection.getXAResource());
                                        statement = connection.createStatement();
                                        statement.executeUpdate("delete from " + entry.getTableName());
                                    }
                                }
                            }
                            transactionManager.commit();
                        } finally {
                            if (null != statement) {
                                try {
                                    statement.close();
                                } catch (SQLException e) {
                                }
                                statement = null;
                            }
                            if (null != connection) {
                                try {
                                    connection.close();
                                } catch (SQLException e) {
                                }
                                connection = null;
                            }
                            if (null != xaConnection) {
                                try {
                                    xaConnection.close();
                                } catch (Exception e) {
                                }
                                xaConnection = null;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }
