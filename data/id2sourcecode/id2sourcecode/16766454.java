    protected final void insert(String tableName, String[] columns, Object[] values) throws RecordException {
        if (columns == null || columns.length == 0) {
            throw new RecordException("No column names passed to the function");
        }
        if (values == null || values.length == 0) {
            throw new RecordException("No values passed to the function");
        }
        if (columns.length != values.length) {
            throw new RecordException("The number of column names and of values does not match");
        }
        String sql = "insert into " + TableNameResolver.getTableName(tableName, isItalian()) + " (";
        for (int i = 0; i < columns.length; ++i) {
            sql += columns[i];
            if (i != columns.length - 1) {
                sql += ", ";
            }
        }
        sql += ") values (";
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
            throw new RecordException("Error inserting into table " + TableNameResolver.getTableName(tableName, isItalian()) + ". A rollback was done.", e);
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
