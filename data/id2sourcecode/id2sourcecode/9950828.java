    public final void deleteAll() throws RecordException {
        Connection conn = ConnectionManager.getConnection();
        LoggableStatement pStat = null;
        Class<? extends Record> actualClass = this.getClass();
        try {
            StatementBuilder builder = new StatementBuilder("delete from " + TableNameResolver.getTableName(actualClass));
            pStat = builder.getPreparedStatement(conn);
            log.log(pStat.getQueryString());
            pStat.executeUpdate();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException(e);
        } finally {
            try {
                if (pStat != null) {
                    pStat.close();
                }
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                throw new RecordException("Error closing connection");
            }
        }
    }
