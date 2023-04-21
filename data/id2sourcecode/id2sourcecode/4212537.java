    public DBSubset loadAtoms(JDCConnection oConn, int iWorkerThreads) throws SQLException {
        PreparedStatement oCmdsStmt;
        PreparedStatement oJobStmt;
        ResultSet oCmdsSet;
        DBSubset oJobsSet;
        int iJobCount;
        String aParams[];
        String aVariable[];
        Properties oParams;
        DistributionList oDistribList;
        Date dtNow = new Date();
        Date dtExec;
        String sSQL;
        int iLoaded = 0;
        if (DebugFile.trace) {
            DebugFile.writeln("Begin AtomFeeder.loadAtoms([Connection], " + String.valueOf(iWorkerThreads) + ")");
            DebugFile.incIdent();
        }
        oJobsSet = new DBSubset(DB.k_jobs, "gu_job,gu_job_group,gu_workarea,id_command,tx_parameters,id_status,dt_execution,dt_finished,dt_created,dt_modified", DB.id_status + "=" + String.valueOf(Job.STATUS_PENDING) + " ORDER BY " + DB.dt_execution + " DESC", iWorkerThreads);
        oJobsSet.setMaxRows(iWorkerThreads);
        iJobCount = oJobsSet.load(oConn);
        sSQL = "UPDATE " + DB.k_jobs + " SET " + DB.id_status + "=" + String.valueOf(Job.STATUS_RUNNING) + "," + DB.dt_execution + "=" + DBBind.Functions.GETDATE + " WHERE " + DB.gu_job + "=?";
        if (DebugFile.trace) DebugFile.writeln("Connection.prepareStatement(" + sSQL + ")");
        oJobStmt = oConn.prepareStatement(sSQL);
        for (int j = 0; j < iJobCount; j++) {
            oParams = parseParameters(oJobsSet.getString(4, j));
            if (oParams.getProperty("gu_list") != null) {
                oDistribList = new DistributionList(oConn, oParams.getProperty("gu_list"));
                if (oDistribList.isNull(DB.dt_execution)) dtExec = dtNow; else dtExec = oDistribList.getDate(DB.dt_execution);
                switch(oDistribList.getShort(DB.tp_list)) {
                    case DistributionList.TYPE_DYNAMIC:
                        iLoaded += loadDynamicList(oConn, oJobsSet.getString(0, j), dtExec, oParams.getProperty("gu_list"), oDistribList.getString(DB.gu_query), oDistribList.getString(DB.gu_workarea));
                        break;
                    case DistributionList.TYPE_STATIC:
                        iLoaded += loadStaticList(oConn, oJobsSet.getString(0, j), dtExec, oParams.getProperty("gu_list"));
                        break;
                    case DistributionList.TYPE_DIRECT:
                        iLoaded += loadDirectList(oConn, oJobsSet.getString(0, j), dtExec, oParams.getProperty("gu_list"));
                        break;
                }
            } else iLoaded = 0;
            if (DebugFile.trace) DebugFile.writeln("PrepareStatement.setString(1, '" + oJobsSet.getStringNull(0, j, "") + "')");
            oJobStmt.setString(1, oJobsSet.getString(0, j));
            if (DebugFile.trace) DebugFile.writeln("PrepareStatement.executeUpdate()");
            oJobStmt.executeUpdate();
        }
        if (DebugFile.trace) DebugFile.writeln("PrepareStatement.close()");
        oJobStmt.close();
        if (DebugFile.trace) {
            DebugFile.decIdent();
            DebugFile.writeln("End AtomFeeder.loadAtoms() : " + String.valueOf(oJobsSet.getRowCount()));
        }
        return oJobsSet;
    }
