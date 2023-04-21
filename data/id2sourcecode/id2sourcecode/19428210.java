    public void insertEventLogBlock(List<EventLog> eventLogs) {
        Connection connection = null;
        NamedParameterStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            for (EventLog eventLog : eventLogs) {
                try {
                    statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_EVENTLOG));
                    statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_LOGGER, eventLog.getLogger());
                    statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_MESSAGE, eventLog.getMessage());
                    statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_STACKTRACE, eventLog.getStacktrace());
                    statement.setString(QueryStore.INSERT_EVENTLOG_PARAM_PRIORITY, eventLog.getPriority());
                    statement.setTimestamp(QueryStore.INSERT_EVENTLOG_PARAM_CREATIONTIME, new Timestamp(eventLog.getCreationTime().getTime()));
                    statement.executeUpdate();
                } catch (Exception e) {
                } finally {
                    close(statement);
                }
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
            }
            System.err.println("Error inserting event log in database: " + e.getMessage());
        } finally {
            close(connection);
        }
    }
