    void setMonitorStatus(int id, AiringMonitorStatus status, long start, boolean fireRecSchedEvent) {
        AiringMonitorStatus prev = getMonitorStatus(id);
        long nextUpdate = Math.min(start - 1800000, System.currentTimeMillis() + 86400000);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        cal.setTimeInMillis(start);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (cal.getTimeInMillis() > System.currentTimeMillis()) nextUpdate = Math.min(nextUpdate, cal.getTimeInMillis());
        boolean autoCommit = true;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            monitorRmQry.setInt(1, id);
            monitorRmQry.executeUpdate();
            monitorInsQry.setInt(1, id);
            monitorInsQry.setString(2, status.toString());
            monitorInsQry.setTimestamp(3, new Timestamp(nextUpdate));
            monitorInsQry.executeUpdate();
            conn.commit();
            if (fireRecSchedEvent && status != prev && (API.apiNullUI.global.IsServerUI() || API.apiNullUI.global.IsRemoteUI())) RecSchedServiceImpl.recSchedChanged();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error(SQL_ERROR, e1);
            }
            LOG.error(SQL_ERROR, e);
        } finally {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                LOG.error(SQL_ERROR, e);
            }
        }
    }
