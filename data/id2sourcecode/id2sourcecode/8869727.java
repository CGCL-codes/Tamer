    public static void batchSave(List<ExperimentResult> v, Tasks task) throws SQLException {
        if (v.isEmpty()) {
            return;
        }
        if (v.size() > 10000) {
            boolean autoCommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
            try {
                DatabaseConnector.getInstance().getConn().setAutoCommit(false);
                ArrayList<ExperimentResult> res = new ArrayList<ExperimentResult>();
                int jobsCreated = 0;
                for (ExperimentResult ex : v) {
                    res.add(ex);
                    if (res.size() == 10000) {
                        batchSave(res);
                        jobsCreated += 10000;
                        if (task != null) {
                            task.setTaskProgress(jobsCreated / (float) v.size());
                        }
                        res.clear();
                    }
                }
                if (!res.isEmpty()) {
                    batchSave(res);
                }
                if (task != null) {
                    task.setTaskProgress(0.f);
                }
            } catch (SQLException ex) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
                throw ex;
            } catch (Error ex) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
                throw ex;
            } catch (Throwable ex) {
                if (autoCommit) {
                    DatabaseConnector.getInstance().getConn().rollback();
                }
            } finally {
                DatabaseConnector.getInstance().getConn().setAutoCommit(autoCommit);
            }
            return;
        }
        boolean autoCommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
        try {
            DatabaseConnector.getInstance().getConn().setAutoCommit(false);
            String query = getInsertQuery(v.size());
            PreparedStatement st = DatabaseConnector.getInstance().getConn().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int curCount = 1;
            curSt = st;
            for (ExperimentResult r : v) {
                st.setInt(curCount++, r.getSolverConfigId());
                st.setInt(curCount++, r.getExperimentId());
                st.setInt(curCount++, r.getInstanceId());
                st.setInt(curCount++, r.getRun());
                st.setInt(curCount++, r.getStatus().getStatusCode());
                st.setInt(curCount++, r.getSeed());
                st.setTimestamp(curCount++, r.getStartTime());
                st.setInt(curCount++, r.getPriority());
                st.setFloat(curCount++, r.getResultTime());
                st.setInt(curCount++, r.getComputeQueue());
                st.setInt(curCount++, r.getResultCode().getResultCode());
                st.setInt(curCount++, r.getCPUTimeLimit());
                st.setInt(curCount++, r.getMemoryLimit());
                st.setInt(curCount++, r.getWallClockTimeLimit());
                st.setInt(curCount++, r.getStackSizeLimit());
            }
            st.executeUpdate();
            ResultSet rs = st.getGeneratedKeys();
            int i = 0;
            while (rs.next()) {
                v.get(i).setSaved();
                v.get(i).setId(rs.getInt(1));
                i++;
            }
            rs.close();
            st.close();
            query = getInsertOutputsQuery(v.size());
            st = DatabaseConnector.getInstance().getConn().prepareStatement(query);
            curSt = st;
            curCount = 1;
            for (ExperimentResult r : v) {
                st.setInt(curCount++, r.getId());
                if (r instanceof ExperimentResultEx) {
                    ExperimentResultEx rx = (ExperimentResultEx) r;
                    st.setBytes(curCount++, rx.getSolverOutput());
                    st.setBytes(curCount++, rx.getLauncherOutput());
                    st.setBytes(curCount++, rx.getWatcherOutput());
                    st.setBytes(curCount++, rx.getVerifierOutput());
                } else {
                    st.setNull(curCount++, java.sql.Types.BLOB);
                    st.setNull(curCount++, java.sql.Types.BLOB);
                    st.setNull(curCount++, java.sql.Types.BLOB);
                    st.setNull(curCount++, java.sql.Types.BLOB);
                }
                st.setNull(curCount++, java.sql.Types.INTEGER);
                st.setNull(curCount++, java.sql.Types.INTEGER);
                st.setNull(curCount++, java.sql.Types.INTEGER);
            }
            st.executeUpdate();
            st.close();
        } catch (SQLException ex) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
            throw ex;
        } catch (Error ex) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
            throw ex;
        } catch (Throwable ex) {
            if (autoCommit) {
                DatabaseConnector.getInstance().getConn().rollback();
            }
        } finally {
            curSt = null;
            DatabaseConnector.getInstance().getConn().setAutoCommit(autoCommit);
        }
    }
