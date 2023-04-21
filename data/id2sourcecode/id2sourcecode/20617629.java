    public synchronized void store(final String alias, final ActionQueueEntry action) throws TeqloException {
        try {
            wrapWithDeadlockRetry("store", new JDBCTransactionWrapper<Object>() {

                public String run(Connection connection) throws SQLException {
                    PreparedStatement stmt = storeStmts.get(connection);
                    if (stmt == null) {
                        stmt = connection.prepareStatement("INSERT INTO tq_actions (aliasName, queueTime, fromAlias, toAlias, topic, channel, actionData) VALUES (?, ?, ?, ?, ?, ?, ?)");
                        storeStmts.put(connection, stmt);
                    }
                    long when = System.currentTimeMillis();
                    stmt.setString(1, alias);
                    stmt.setTimestamp(2, new Timestamp(when));
                    stmt.setString(3, action.getFromAlias());
                    stmt.setString(4, action.getToAlias());
                    stmt.setString(5, action.getTopic());
                    stmt.setString(6, action.getChannel());
                    stmt.setString(7, action.getData());
                    stmt.executeUpdate();
                    return null;
                }
            });
        } catch (SQLException e) {
            throw new TeqloException(this, alias, e, "Could not insert record into table.");
        }
    }
