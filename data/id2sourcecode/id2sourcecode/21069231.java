        public void writeToSQL(Connection conn, String schema, SnapShotData data) throws SQLException {
            writeToSQL(conn, schema, (ThreadPoolMonitor) data);
        }
