    @Override
    public List<Integer> executeUpdateGetGeneratedKeys(IBBDBeanAPI storedProcedure, Object... params) throws SQLException {
        ArrayList<Integer> sqlAL = new ArrayList();
        Statement stmt = null;
        PreparedStatement pstmt = null;
        int updatedRows = 0;
        ResultSet rs = null;
        BBDAPIPrincipal principal = (BBDAPIPrincipal) ((BBDBeanAPI) storedProcedure).getBbdPrincipal();
        boolean mySQL = true;
        try {
            if (params.length == 0) {
                stmt = getConnection(principal).createStatement();
                updatedRows = stmt.executeUpdate(getSQL(storedProcedure), Statement.RETURN_GENERATED_KEYS);
                if (mySQL) {
                    rs = stmt.getResultSet();
                } else {
                    rs = stmt.getGeneratedKeys();
                }
            } else {
                pstmt = getConnection(principal).prepareStatement(getSQL(storedProcedure), Statement.RETURN_GENERATED_KEYS);
                insertArguments(storedProcedure, params, pstmt);
                updatedRows = pstmt.executeUpdate();
                if (mySQL) {
                    rs = pstmt.getResultSet();
                } else {
                    rs = pstmt.getGeneratedKeys();
                }
            }
            int rowsChanged = -1;
            int endOfKeysIndex = -1;
            if (mySQL) {
                while (rs.next()) {
                    rowsChanged = rs.getInt(1);
                    sqlAL.add(rowsChanged);
                    endOfKeysIndex = rowsChanged + 2;
                    for (int i = 2; i < endOfKeysIndex; i++) {
                        sqlAL.add(rs.getInt(i));
                    }
                }
                if (params.length == 0) {
                    while (stmt.getMoreResults()) {
                        rs = stmt.getResultSet();
                        while (rs.next()) {
                            rowsChanged = rs.getInt(1);
                            sqlAL.add(rowsChanged);
                            endOfKeysIndex = rowsChanged + 2;
                            for (int i = 2; i < endOfKeysIndex; i++) {
                                sqlAL.add(rs.getInt(i));
                            }
                        }
                    }
                } else {
                    while (pstmt.getMoreResults()) {
                        rs = pstmt.getResultSet();
                        while (rs.next()) {
                            rowsChanged = rs.getInt(1);
                            sqlAL.add(rowsChanged);
                            endOfKeysIndex = rowsChanged + 2;
                            for (int i = 2; i < endOfKeysIndex; i++) {
                                sqlAL.add(rs.getInt(i));
                            }
                        }
                    }
                }
            } else {
                sqlAL.add(updatedRows);
                while (rs.next()) {
                    for (int i = 2; i < updatedRows + 2; i++) {
                        sqlAL.add(rs.getInt(i));
                    }
                }
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
            if (rs != null) {
                rs.close();
            }
        }
        return sqlAL;
    }
