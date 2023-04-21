    public final int executeTransaction(final boolean commit, final Transaction transaction) throws SQLException {
        int updated = 0;
        try {
            for (Statement statement : transaction.statements) {
                updated += executeUpdate(false, statement.sql, statement.params);
            }
            if (commit) {
                commit();
            }
        } catch (SQLException e) {
            rollback();
            throw e;
        }
        return updated;
    }
