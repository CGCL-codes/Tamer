    public static void deleteExperimentResults(ArrayList<ExperimentResult> experimentResults, Tasks task) throws SQLException {
        if (experimentResults.isEmpty()) {
            return;
        }
        if (experimentResults.size() > 10000) {
            boolean autoCommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
            try {
                ArrayList<ExperimentResult> res = new ArrayList<ExperimentResult>();
                int deletedCount = 0;
                for (ExperimentResult ex : experimentResults) {
                    res.add(ex);
                    if (res.size() == 10000) {
                        deleteExperimentResults(res, task);
                        deletedCount += 10000;
                        if (task != null) {
                            task.setTaskProgress(deletedCount / (float) experimentResults.size());
                        }
                        res.clear();
                    }
                }
                if (!res.isEmpty()) {
                    deleteExperimentResults(res);
                }
                if (task != null) {
                    task.setTaskProgress(0.f);
                }
            } catch (SQLException e) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
                throw e;
            } catch (Error e) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
                throw e;
            } catch (Throwable e) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
            } finally {
                curSt = null;
                DatabaseConnector.getInstance().getConn().setAutoCommit(autoCommit);
            }
            return;
        }
        boolean autoCommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
        try {
            DatabaseConnector.getInstance().getConn().setAutoCommit(false);
            String query = getDeleteQuery(experimentResults.size());
            PreparedStatement st = DatabaseConnector.getInstance().getConn().prepareStatement(query);
            curSt = st;
            int curCount = 1;
            for (ExperimentResult r : experimentResults) {
                st.setInt(curCount++, r.getId());
                r.setDeleted();
            }
            st.executeUpdate();
            st.close();
            HashMap<Integer, ArrayList<Integer>> clientJobs = new HashMap<Integer, ArrayList<Integer>>();
            for (ExperimentResult r : experimentResults) {
                if (r.getIdClient() == null || !r.getStatus().equals(StatusCode.RUNNING)) {
                    continue;
                }
                ArrayList<Integer> tmp = clientJobs.get(r.getIdClient());
                if (tmp == null) {
                    tmp = new ArrayList<Integer>();
                    clientJobs.put(r.getIdClient(), tmp);
                }
                tmp.add(r.getId());
            }
            for (Integer clientId : clientJobs.keySet()) {
                String message = "";
                for (Integer jobId : clientJobs.get(clientId)) {
                    message += "kill " + jobId + '\n';
                }
                ClientDAO.sendMessage(clientId, message);
            }
        } catch (SQLException e) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
            throw e;
        } catch (Error e) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
            throw e;
        } catch (Throwable e) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
        } finally {
            curSt = null;
            DatabaseConnector.getInstance().getConn().setAutoCommit(autoCommit);
        }
    }
