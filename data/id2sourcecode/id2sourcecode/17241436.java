    public static void save(ConfigurationScenario cs) throws SQLException {
        boolean autocommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
        try {
            DatabaseConnector.getInstance().getConn().setAutoCommit(false);
            if (cs.isNew()) {
                PreparedStatement st = DatabaseConnector.getInstance().getConn().prepareStatement("INSERT INTO ConfigurationScenario (SolverBinaries_idSolverBinary, Experiment_idExperiment) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                st.setInt(1, cs.getIdSolverBinary());
                st.setInt(2, cs.getIdExperiment());
                st.executeUpdate();
                ResultSet generatedKeys = st.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cs.setId(generatedKeys.getInt(1));
                }
                generatedKeys.close();
                st.close();
            } else if (cs.isModified()) {
                PreparedStatement st = DatabaseConnector.getInstance().getConn().prepareStatement("UPDATE ConfigurationScenario SET SolverBinaries_idSolverBinary = ? WHERE Experiment_idExperiment = ?");
                st.setInt(1, cs.getIdSolverBinary());
                st.setInt(2, cs.getIdExperiment());
                st.executeUpdate();
                st.close();
            }
            cs.setSaved();
            Statement st = DatabaseConnector.getInstance().getConn().createStatement();
            st.executeUpdate("DELETE FROM ConfigurationScenario_has_Parameters WHERE ConfigurationScenario_idConfigurationScenario = " + cs.getId());
            st.close();
            PreparedStatement ps = DatabaseConnector.getInstance().getConn().prepareStatement("INSERT INTO ConfigurationScenario_has_Parameters (ConfigurationScenario_idConfigurationScenario, Parameters_idParameter, configurable, fixedValue) VALUES (?, ?, ?, ?)");
            for (ConfigurationScenarioParameter param : cs.getParameters()) {
                ps.setInt(1, cs.getId());
                param.setIdConfigurationScenario(cs.getId());
                ps.setInt(2, param.getIdParameter());
                ps.setBoolean(3, param.isConfigurable());
                ps.setString(4, param.getFixedValue());
                ps.addBatch();
                param.setSaved();
            }
            ps.executeBatch();
            ps.close();
            if (cs.getCourse() != null && !cs.getCourse().isSaved()) {
                PreparedStatement st2 = DatabaseConnector.getInstance().getConn().prepareStatement("INSERT INTO Course (ConfigurationScenario_idConfigurationScenario, Instances_idInstance, seed, `order`) VALUES (?, ?, ?, ?)");
                for (int i = cs.getCourse().getModifiedIndex(); i < cs.getCourse().getLength(); i++) {
                    st2.setInt(1, cs.getId());
                    InstanceSeed is = cs.getCourse().get(i);
                    st2.setInt(2, is.instance.getId());
                    st2.setInt(3, is.seed);
                    st2.setInt(4, i);
                    st2.addBatch();
                }
                st2.executeBatch();
                st2.close();
                Statement stmnt = DatabaseConnector.getInstance().getConn().createStatement();
                stmnt.executeUpdate("UPDATE ConfigurationScenario SET initial_course_length = " + cs.getCourse().getLength() + " WHERE idConfigurationScenario = " + cs.getId());
                stmnt.close();
                cs.getCourse().setInitialLength(cs.getCourse().getLength());
                cs.getCourse().setSaved();
            } else if (cs.getCourse() == null) {
                cs.setCourse(new Course());
            }
        } catch (Exception ex) {
            DatabaseConnector.getInstance().getConn().rollback();
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            }
        } finally {
            DatabaseConnector.getInstance().getConn().setAutoCommit(autocommit);
        }
    }
