    public void installTSQLStoredProcedure(String storedProcedureName, String storedProcSQL, EXISTING_STORED_PROC_STRATEGY existingEndpointStrategy, String jdbcDriverName, String jdbcUrl, String databaseName, String hpUserName, String hpPassword) throws EvilDynamicStoredProcedureException {
        boolean done = false;
        while (!done) {
            try {
                Connection conn = establishDatabaseConnection(jdbcDriverName, jdbcUrl, databaseName, hpUserName, hpPassword);
                executeSQL(storedProcSQL, conn);
                closeDatabaseConnection(conn);
                done = true;
            } catch (SQLException e) {
                switch(e.getErrorCode()) {
                    case ERROR_CODES.ALREADY_THERE:
                        {
                            System.err.println("Stored procedure'" + storedProcedureName + "' already installed in database '" + databaseName + "'");
                            switch(existingEndpointStrategy) {
                                case OVERWRITE_EXISTING_PROCEDURE:
                                    {
                                        System.err.println("Attempting overwrite..");
                                        existingEndpointStrategy = EXISTING_STORED_PROC_STRATEGY.FAIL;
                                        deleteStoredProcedure(storedProcedureName, jdbcDriverName, jdbcUrl, databaseName, hpUserName, hpPassword);
                                        break;
                                    }
                                default:
                                    {
                                        throw new EvilDynamicStoredProcedureException("Not allowed to overwrite existing stored procedure, or tried DROP'ing once already", e);
                                    }
                            }
                            break;
                        }
                    default:
                        {
                            throw new EvilDynamicStoredProcedureException("Unknown SQL error code when attempting install stored procedure: " + e.getErrorCode(), e);
                        }
                }
            }
        }
    }
