    public int runDdl(String userID) throws OutputManagerException {
        ArrayList<String> ddl;
        ArrayList<String> failures;
        try {
            MraldConnection conn;
            String datasource = msg.getValue("Datasource")[0];
            if (datasource.equals("")) {
                datasource = "main";
            }
            conn = new MraldConnection(datasource, msg);
            conn.setAutoCommit(false);
            ddl = failures = new ArrayList<String>(Arrays.asList(msg.getQuery()));
            int lastNumFailed = 0;
            int iterCount = 0;
            while (failures.size() > 0 && failures.size() != lastNumFailed && iterCount < 5) {
                if (iterCount > 5) {
                    break;
                }
                lastNumFailed = failures.size();
                ddl = failures;
                failures = new ArrayList<String>();
                String query;
                for (int i = 0; i < ddl.size(); i++) {
                    query = ddl.get(i);
                    try {
                        StringBuffer logInfo = new StringBuffer();
                        long startTime = MiscUtils.logQuery(userID, datasource, query, logInfo);
                        conn.executeUpdate(query);
                        MiscUtils.logQueryRun(startTime, logInfo);
                    } catch (SQLException sqle) {
                        failures.add(query);
                        MraldOutFile.logToFile(sqle);
                        failMessages.put("Iteration " + iterCount + ", query " + i + " failed: " + query, sqle.getMessage());
                    }
                }
                iterCount++;
            }
            success = failures.size() == 0;
            if (success && iterCount < 100) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.close();
        } catch (SQLException e) {
            throw new OutputManagerException(e);
        }
        return failures.size();
    }
