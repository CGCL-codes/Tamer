    private void writeToSQL(Long parentRowID, ThreadTraceData data, Connection conn, String schema, Map categoryNameCache) throws SQLException {
        Long myRowID = null;
        String s = (schema == null) ? "" : (schema + ".");
        final boolean oracleConnection = JDBCHelper.isOracleConnection(conn);
        String categoryName = data.getName();
        Long categoryID = (Long) categoryNameCache.get(categoryName);
        if (categoryID == null) {
            categoryID = JDBCHelper.simpleGetOrCreate(conn, s + "P4JCategory", "CategoryID", "CategoryName", categoryName);
            categoryNameCache.put(categoryID, categoryName);
        }
        PreparedStatement stmtInsert = null;
        ResultSet rs = null;
        try {
            final String sql = "INSERT INTO " + s + "P4JThreadTrace\r\n" + "	(ParentRowID, CategoryID, StartTime, EndTime, Duration, SQLDuration)\r\n" + "	VALUES(?, ?, ?, ?, ?, ?)";
            if (oracleConnection) {
                stmtInsert = conn.prepareStatement(sql, new int[] { 1 });
            } else {
                stmtInsert = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
            stmtInsert.setObject(1, parentRowID, Types.INTEGER);
            stmtInsert.setLong(2, categoryID.longValue());
            stmtInsert.setTimestamp(3, new Timestamp(data.getStartTime()));
            stmtInsert.setTimestamp(4, new Timestamp(data.getEndTime()));
            stmtInsert.setLong(5, data.getEndTime() - data.getStartTime());
            Long sqlTimeVal = null;
            if (SQLTime.isEnabled()) {
                sqlTimeVal = new Long(Math.max(0, sqlEndTime - sqlStartTime));
            }
            stmtInsert.setObject(6, sqlTimeVal, Types.INTEGER);
            stmtInsert.execute();
            rs = stmtInsert.getGeneratedKeys();
            rs.next();
            myRowID = new Long(rs.getLong(1));
        } finally {
            JDBCHelper.closeNoThrow(rs);
            JDBCHelper.closeNoThrow(stmtInsert);
        }
        ThreadTraceData children[] = data.getChildren();
        for (int i = 0; i < children.length; i++) {
            writeToSQL(myRowID, children[i], conn, schema, categoryNameCache);
        }
    }
