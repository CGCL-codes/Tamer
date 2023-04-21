    public void renameConcernDomain(String oldName, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_CONCERN_DOMAIN_NAME, newName, oldName);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }
