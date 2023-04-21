    public Concern createConcern(IConcernListener changeListener, String name, String shortName, String description, String color) {
        try {
            Integer id = getNextSequenceNumber("concern_id_seq", CONCERN_TABLE);
            List<Object> params = new ArrayList<Object>();
            params.add(id);
            params.add(name);
            params.add(shortName);
            params.add(description);
            params.add(color);
            PreparedStatement statement = createPreparedStatement(CONCERN_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return getConcern(id, changeListener);
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().indexOf("Violation of unique constraint") >= 0) {
                ProblemManager.reportException(e, "Failed to create concern. Concern '" + name + "' already exists.");
            } else {
                ProblemManager.reportException(e, "Failed to create concern '" + name + "'.");
            }
            return null;
        }
    }
