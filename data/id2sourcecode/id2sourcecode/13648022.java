    @Override
    public int executeUpdate(final IBBDBeanAPI storedProcedure, final Object... params) throws SQLException {
        int rowsUpdated = 0;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        BBDAPIPrincipal principal = (BBDAPIPrincipal) ((BBDBeanAPI) storedProcedure).getBbdPrincipal();
        try {
            if (params.length == 0) {
                stmt = getConnection(principal).createStatement();
                rowsUpdated = stmt.executeUpdate(getSQL(storedProcedure));
                logWarnings(stmt.getWarnings());
            } else {
                pstmt = getConnection(principal).prepareStatement(getSQL(storedProcedure));
                insertArguments(storedProcedure, params, pstmt);
                rowsUpdated = pstmt.executeUpdate();
                logWarnings(pstmt.getWarnings());
            }
            if (!getConnection(principal).getAutoCommit()) {
                getConnection(principal).commit();
            }
        } catch (SQLException sqle) {
            logExceptions(sqle);
            if (!getConnection(principal).getAutoCommit()) {
                getConnection(principal).rollback();
            }
            throw sqle;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return rowsUpdated;
    }
