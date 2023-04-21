    public Boolean executeUpdateTransaction(DatabaseQuery[] aDatabaseQueries) {
        Integer iPos = 0;
        PreparedStatement[] aQueries = this.getPreparedStatements(aDatabaseQueries);
        Connection oConnection = this.getConnection();
        Boolean bAutoCommit = true;
        if (aQueries.length == 0) {
            return true;
        }
        if (oConnection == null) {
            return false;
        }
        try {
            bAutoCommit = oConnection.getAutoCommit();
            oConnection.setAutoCommit(false);
            for (iPos = 0; iPos < aQueries.length; iPos++) {
                aQueries[iPos].executeUpdate();
                aQueries[iPos].close();
            }
            if (bAutoCommit) {
                oConnection.commit();
                oConnection.setAutoCommit(true);
            }
        } catch (SQLException oException) {
            if (this.reloadConnection()) return this.executeUpdateTransaction(aDatabaseQueries); else {
                try {
                    oConnection.rollback();
                } catch (SQLException oRollbackException) {
                    throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, Strings.ROLLBACK, oRollbackException);
                }
                throw new DatabaseException(ErrorCode.DATABASE_UPDATE_QUERY, this.getQueries(aDatabaseQueries).toString(), oException);
            }
        } finally {
            try {
                oConnection.setAutoCommit(bAutoCommit);
            } catch (SQLException oException) {
                throw new SystemException(ErrorCode.CLOSE_QUERY, null, oException);
            }
        }
        return true;
    }
