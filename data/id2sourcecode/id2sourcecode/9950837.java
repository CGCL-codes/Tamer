    public int updateBySql(String sql) throws RecordException {
        LoggableStatement pStat = null;
        Connection conn = ConnectionManager.getConnection();
        try {
            pStat = new LoggableStatement(conn, sql);
            log.log(pStat.getQueryString());
            int res = pStat.executeUpdate();
            return res;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback");
            }
            throw new RecordException("Error getting table data", e);
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
