        public void writeToSQL(Connection conn, String schema, ThreadPoolMonitor data) throws SQLException {
            schema = (schema == null) ? "" : (schema + ".");
            final String SQL = "INSERT INTO " + schema + "P4JThreadPoolMonitor " + "(ThreadPoolOwner, InstanceName, StartTime, EndTime, Duration,  " + "CurrentThreadsBusy, CurrentThreadCount) " + "VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(SQL);
                stmt.setString(1, "Apache/Tomcat");
                stmt.setString(2, data.getInstanceName());
                stmt.setTimestamp(3, new Timestamp(data.getStartTime()));
                stmt.setTimestamp(4, new Timestamp(data.getEndTime()));
                stmt.setLong(5, data.getDuration());
                stmt.setLong(6, data.getCurrentThreadsBusy());
                stmt.setLong(7, data.getCurrentThreadCount());
                int count = stmt.executeUpdate();
                if (count != 1) {
                    throw new SQLException("ThreadPoolMonitor failed to insert row");
                }
            } finally {
                JDBCHelper.closeNoThrow(stmt);
            }
        }
