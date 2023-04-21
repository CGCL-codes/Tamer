    public void testDoIt() throws Exception {
        ThreadPoolMonitorImpl.SQLWriter writer = new ThreadPoolMonitorImpl.SQLWriter();
        ThreadPoolMonitor data = Mockito.mock(ThreadPoolMonitor.class);
        long start = System.currentTimeMillis();
        long end = start + 60000;
        Mockito.when(data.getStartTime()).thenReturn(new Long(start));
        Mockito.when(data.getStartTime()).thenReturn(new Long(end));
        Mockito.when(data.getInstanceName()).thenReturn("HTTP");
        Mockito.when(data.getCurrentThreadsBusy()).thenReturn(new Long(25));
        Mockito.when(data.getCurrentThreadCount()).thenReturn(new Long(125));
        writer.writeToSQL(conn, "p4j", data);
        final String VALIDATE_SQL = "SELECT COUNT(*) FROM p4j.P4JThreadPoolMonitor " + " WHERE ThreadPoolOwner=? " + " AND InstanceName=? " + " AND StartTime=? " + " AND EndTime=? " + " AND Duration=? " + " AND CurrentThreadsBusy=? " + " AND CurrentThreadCount=? ";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(VALIDATE_SQL);
            stmt.setString(1, "Apache/Tomcat");
            stmt.setString(2, "HTTP");
            stmt.setTimestamp(3, new Timestamp(data.getStartTime()));
            stmt.setTimestamp(4, new Timestamp(data.getEndTime()));
            stmt.setLong(5, data.getDuration());
            stmt.setLong(6, data.getCurrentThreadsBusy());
            stmt.setLong(7, data.getCurrentThreadCount());
            long resultCount = JDBCHelper.getQueryCount(stmt);
            assertEquals("Should have inserted row", 1, resultCount);
        } finally {
            JDBCHelper.closeNoThrow(stmt);
        }
    }
