    public void renameComponent(Integer componentId, String newName) {
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_COMPONENT_NAME, newName, componentId);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }
