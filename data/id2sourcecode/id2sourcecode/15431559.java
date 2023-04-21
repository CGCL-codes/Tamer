    public ProgramProfilingSymbol createNewProfilingSymbol(int configID, int programSymbolID) throws AdaptationException {
        ProgramProfilingSymbol profilingSymbol = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "INSERT INTO ProgramProfilingSymbols" + "(projectDeploymentConfigurationID, programSymbolID)" + " VALUES (" + configID + ", " + programSymbolID + ")";
            connection = DriverManager.getConnection(CONN_STR);
            statement = connection.createStatement();
            statement.executeUpdate(query);
            query = "SELECT * FROM ProgramProfilingSymbols WHERE " + "projectDeploymentConfigurationID = " + configID + " AND programSymbolID             = " + programSymbolID;
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                connection.rollback();
                String msg = "Attempt to create program profiling " + "symbol failed.";
                log.error(msg);
                throw new AdaptationException(msg);
            }
            profilingSymbol = getProfilingSymbol(resultSet);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (Exception e) {
            }
            String msg = "SQLException in createNewProfilingSymbol";
            log.error(msg, ex);
            throw new AdaptationException(msg, ex);
        } finally {
            try {
                resultSet.close();
            } catch (Exception ex) {
            }
            try {
                statement.close();
            } catch (Exception ex) {
            }
            try {
                connection.close();
            } catch (Exception ex) {
            }
        }
        return profilingSymbol;
    }
