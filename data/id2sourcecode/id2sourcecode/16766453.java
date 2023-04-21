    private final void insert(String tableName, boolean resolveName, Object... values) throws RecordException {
        if (values == null || values.length == 0) {
            throw new RecordException("No values passed to the function");
        }
        String name = null;
        if (resolveName) {
            name = TableNameResolver.getTableName(tableName, isItalian());
        } else {
            name = tableName;
        }
        String sql = "insert into " + name + " values (";
        for (int i = 0; i < values.length; ++i) {
            sql += "?";
            if (i != values.length - 1) {
                sql += ", ";
            }
        }
        sql += ")";
        LoggableStatement st = null;
        Connection conn = null;
        if (autoCommit) {
            conn = ConnectionManager.getConnection();
        } else {
            conn = this.conn;
        }
        try {
            st = new LoggableStatement(conn, sql);
            for (int i = 0; i < values.length; ++i) {
                st.setObject(i + 1, values[i]);
            }
            log.log(st.getQueryString());
            st.executeUpdate();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RecordException("Error executing rollback ", e1);
            }
            throw new RecordException("Error inserting into table " + name + ". A rollback was done.", e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (autoCommit) {
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RecordException("Error closing the connection", e);
            }
        }
    }
