    public void updateSourceRange(Integer componentId, Integer beginLine, Integer beginColumn, Integer endLine, Integer endColumn, Integer numLines) {
        List<Object> params = new ArrayList<Object>();
        params.add(beginLine);
        params.add(beginColumn);
        params.add(endLine);
        params.add(endColumn);
        params.add(numLines);
        params.add(componentId);
        try {
            PreparedStatement statement = createPreparedStatement(UPDATE_COMPONENT_SOURCE_RANGE, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
    }
