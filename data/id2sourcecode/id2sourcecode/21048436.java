    public void writeSonar(int distance, int angle, int seqn, int reading) throws SQLException {
        final String tableName = "sonar";
        Statement stmt = null;
        ResultSet rs = null;
        stmt = conn.createStatement();
        String whereClause = " WHERE distance=" + distance + " AND angle=" + angle + " AND seqn=" + seqn;
        String sql = "SELECT * FROM " + tableName + whereClause;
        stmt.executeQuery(sql);
        rs = stmt.getResultSet();
        if (rs.next()) {
            sql = "UPDATE " + tableName + " SET reading=" + reading + whereClause;
        } else {
            sql = "INSERT INTO " + tableName + " VALUES(" + distance + "," + angle + "," + seqn + "," + reading + ")";
        }
        stmt.executeUpdate(sql);
    }
