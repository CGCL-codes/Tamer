    boolean applyUpdateStatements(List statements) {
        boolean applyUpdateStatements = false;
        if (statements != null) {
            Session aSession = null;
            Connection aConnection = null;
            try {
                aSession = this.createSession();
                if (aSession != null) {
                    aConnection = aSession.connection();
                    if (aConnection != null) {
                        aConnection.commit();
                        aConnection.setAutoCommit(true);
                        Iterator aStatementIterator = statements.iterator();
                        while (aStatementIterator.hasNext()) {
                            String aStatementString = (String) aStatementIterator.next();
                            LOG.debug("applying statement: " + aStatementString);
                            Statement aStatement = aConnection.createStatement();
                            aStatement.executeUpdate(aStatementString);
                            aStatement.close();
                        }
                        applyUpdateStatements = true;
                    }
                }
            } catch (Exception anException) {
                LOG.warn(null, anException);
                try {
                    if (aConnection != null) {
                        aConnection.rollback();
                    }
                } catch (SQLException e) {
                    LOG.warn(null, e);
                }
                applyUpdateStatements = false;
            } finally {
                try {
                    if (aConnection != null) {
                        JDBCExceptionReporter.logWarnings(aConnection.getWarnings());
                        aConnection.clearWarnings();
                    }
                    if (aSession != null) {
                        aSession.close();
                    }
                } catch (Exception anException) {
                    applyUpdateStatements = false;
                    LOG.warn(null, anException);
                }
            }
        }
        return applyUpdateStatements;
    }
