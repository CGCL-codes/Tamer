    private String addSynonym(Entry entry) throws SQLException {
        conn.setAutoCommit(false);
        int defaultLevel = conn.getTransactionIsolation();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        int result = 0;
        String statement = "";
        try {
            if (entry.preferredFlag.equals("T")) {
                statement = getDAO().getStatement(TABLE_KEY, "SET_PREFERRED_TERM_FALSE2");
                statement = getDAO().getStatement(statement, 1, String.valueOf(entry.fromGID));
                statement += entry.associationGID;
                result = keepAliveStmt.executeUpdate(statement);
            }
            statement = getDAO().getStatement(TABLE_KEY, "ADD_SYNONYM_ASSOCIATION");
            statement = getDAO().getStatement(statement, 1, String.valueOf(entry.fromGID));
            statement = getDAO().getStatement(statement, 2, String.valueOf(entry.associationGID));
            statement = getDAO().getStatement(statement, 3, String.valueOf(entry.toGID));
            statement = getDAO().getStatement(statement, 4, "'" + String.valueOf(entry.preferredFlag) + "'");
            result = keepAliveStmt.executeUpdate(statement);
            if (result == 0) {
                conn.rollback();
                return getFalseResult();
            }
            WFPlugin wf = this.getConceptWF(entry.permit);
            wf.update(entry.fromId, entry.fromNamespaceId, entry.permit, WFPlugin.ATTR_SYNONYM, WFPlugin.EDIT_ADD);
            conn.commit();
            return getTrueResult();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
        }
    }
