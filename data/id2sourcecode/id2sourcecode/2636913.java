    private String updateSynonym(Entry oldEntry, Entry newEntry) throws SQLException {
        conn.setAutoCommit(false);
        int defaultLevel = conn.getTransactionIsolation();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        int result = 0;
        String statement = "";
        try {
            if (newEntry.preferredFlag.equals("T")) {
                statement = getDAO().getStatement(TABLE_KEY, "SET_PREFERRED_TERM_FALSE2");
                statement = getDAO().getStatement(statement, 1, String.valueOf(newEntry.fromGID));
                statement += newEntry.associationGID;
                result = keepAliveStmt.executeUpdate(statement);
            }
            statement = getDAO().getStatement(TABLE_KEY, "UPDATE_" + oldEntry.mode + "_ASSOCIATION");
            statement = getDAO().getStatement(statement, 1, String.valueOf(newEntry.fromGID));
            statement = getDAO().getStatement(statement, 2, String.valueOf(newEntry.associationGID));
            statement = getDAO().getStatement(statement, 3, String.valueOf(newEntry.toGID));
            statement = getDAO().getStatement(statement, 4, "'" + String.valueOf(newEntry.preferredFlag) + "'");
            statement = getDAO().getStatement(statement, 5, String.valueOf(oldEntry.fromGID));
            statement = getDAO().getStatement(statement, 6, String.valueOf(oldEntry.associationGID));
            statement += oldEntry.toGID;
            result = keepAliveStmt.executeUpdate(statement);
            String response = null;
            if (result != 1) {
                conn.rollback();
                return getFalseResult();
            }
            response = getTrueResult();
            WFPlugin wf = this.getConceptWF(oldEntry.permit);
            wf.update(oldEntry.fromId, oldEntry.fromNamespaceId, oldEntry.permit, WFPlugin.ATTR_SYNONYM, WFPlugin.EDIT_UPDATE);
            conn.commit();
            return response;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
        }
    }
