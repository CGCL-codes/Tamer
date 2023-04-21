    public void renameConcern(int concernId, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_CONCERN_NAME, newName, concernId);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }
