    public void exposeStoredProcedureFromDotNetAssembly(Config.StoredProc spConf, String assemblyName, EXISTING_STORED_PROC_STRATEGY storedProcStrategy, String jdbcDriverName, String jdbcUrl, String databaseName, String hpUserName, String hpPassword) throws EvilDynamicStoredProcedureException {
        String createProcedureSQL = "CREATE PROCEDURE " + spConf.toString() + " AS EXTERNAL NAME " + assemblyName + ".StoredProcedures." + spConf.name;
        boolean done = false;
        while (!done) {
            try {
                Connection conn = establishDatabaseConnection(jdbcDriverName, jdbcUrl, databaseName, hpUserName, hpPassword);
                executeSQL(createProcedureSQL, conn);
                closeDatabaseConnection(conn);
                done = true;
            } catch (SQLException e) {
                switch(e.getErrorCode()) {
                    case ERROR_CODES.ALREADY_THERE:
                        {
                            System.err.println("Stored procedure'" + spConf.name + "' already installed in database '" + databaseName + "'");
                            switch(storedProcStrategy) {
                                case OVERWRITE_EXISTING_PROCEDURE:
                                    {
                                        System.err.println("Attempting overwrite..");
                                        try {
                                            Connection conn = establishDatabaseConnection(jdbcDriverName, jdbcUrl, databaseName, hpUserName, hpPassword);
                                            executeSQL("DROP PROCEDURE " + spConf.name, conn);
                                            closeDatabaseConnection(conn);
                                            storedProcStrategy = EXISTING_STORED_PROC_STRATEGY.FAIL;
                                        } catch (SQLException e2) {
                                            throw new EvilDynamicStoredProcedureException("Can't seem to DROP already installed procedure, bailing out", e2);
                                        }
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
                            throw new EvilDynamicStoredProcedureException("Unknown SQL error code when attempting install stored procedure: " + createProcedureSQL + e.getErrorCode(), e);
                        }
                }
            }
        }
    }
