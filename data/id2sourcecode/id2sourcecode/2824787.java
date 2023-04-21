    public void run() {
        Statement oStmt;
        ResultSet oRSet;
        int iJobCount;
        try {
            if (DebugFile.trace) DebugFile.writeln("new JDCConnection(" + oEnvProps.getProperty("dburl") + "," + oEnvProps.getProperty("dbuser") + ", ...)");
            oCon = new JDCConnection(DriverManager.getConnection(oEnvProps.getProperty("dburl"), oEnvProps.getProperty("dbuser"), oEnvProps.getProperty("dbpassword")), null);
            if (DebugFile.trace) DebugFile.writeln("JDCConnection.setAutoCommit(true)");
            oCon.setAutoCommit(true);
            if (DebugFile.trace) DebugFile.writeln("new AtomQueue()");
            oQue = new AtomQueue();
            if (DebugFile.trace) DebugFile.writeln("new AtomFeeder()");
            AtomFeeder oFdr = new AtomFeeder();
            if (DebugFile.trace) DebugFile.writeln("new AtomConsumer([JDCconnection], [AtomQueue])");
            AtomConsumer oCsr = new AtomConsumer(oCon, oQue);
            if (DebugFile.trace) DebugFile.writeln("new WorkerThreadPool([AtomConsumer], [Properties])");
            oThreadPool = new WorkerThreadPool(oCsr, oEnvProps);
            ListIterator oIter = oCallbacks.listIterator();
            while (oIter.hasNext()) oThreadPool.registerCallback((WorkerThreadCallback) oIter.next());
            do {
                try {
                    while (bContinue) {
                        oStmt = oCon.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                        try {
                            oStmt.setQueryTimeout(20);
                        } catch (SQLException sqle) {
                        }
                        if (DebugFile.trace) DebugFile.writeln("Statement.executeQuery(SELECT COUNT(*) FROM k_jobs WHERE id_status=" + String.valueOf(Job.STATUS_PENDING) + ")");
                        oRSet = oStmt.executeQuery("SELECT COUNT(*) FROM k_jobs WHERE id_status=" + String.valueOf(Job.STATUS_PENDING));
                        oRSet.next();
                        iJobCount = oRSet.getInt(1);
                        oRSet.close();
                        oStmt.close();
                        if (DebugFile.trace) DebugFile.writeln(String.valueOf(iJobCount) + " pending jobs");
                        if (0 == iJobCount) sleep(10000); else break;
                    }
                    if (bContinue) {
                        oFdr.loadAtoms(oCon, oThreadPool.size());
                        oFdr.feedQueue(oCon, oQue);
                        if (oQue.size() > 0) oThreadPool.launchAll();
                        do {
                            sleep(10000);
                            if (DebugFile.trace) DebugFile.writeln(String.valueOf(oThreadPool.livethreads()) + " live threads");
                        } while (oThreadPool.livethreads() == oThreadPool.size());
                    }
                } catch (InterruptedException e) {
                    if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon InterruptedException " + e.getMessage());
                }
            } while (bContinue);
            if (DebugFile.trace) DebugFile.writeln(" exiting SchedulerDaemon");
            oThreadPool = null;
            oCsr.close();
            oCsr = null;
            oFdr = null;
            oQue = null;
            if (DebugFile.trace) DebugFile.writeln("JDConnection.close()");
            oCon.close();
            oCon = null;
        } catch (SQLException e) {
            try {
                if (oCon != null) if (!oCon.isClosed()) oCon.close();
            } catch (SQLException sqle) {
                if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon SQLException on close() " + sqle.getMessage());
            }
            oCon = null;
            if (DebugFile.trace) DebugFile.writeln("SchedulerDaemon SQLException " + e.getMessage());
        }
    }
