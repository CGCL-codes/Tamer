    public ConcernDomain createConcernDomain(String name, String description, String shortName, String kind, IConcernListener changeListener) {
        Concern rootConcern = getOrCreateRootConcern(name, changeListener);
        if (rootConcern == null) return null;
        List<Object> params = new ArrayList<Object>();
        params.add(rootConcern.getId());
        params.add(name);
        params.add(shortName);
        params.add(description);
        params.add(kind);
        try {
            PreparedStatement statement = createPreparedStatement(CONCERN_DOMAIN_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return getConcernDomain(name, changeListener);
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().indexOf("Violation of unique constraint") >= 0) {
                ProblemManager.reportException(e, "Failed to create concern domain. Concern domain '" + name + "' already exists.");
            } else {
                ProblemManager.reportException(e, "Failed to create concern domain '" + name + "'.");
            }
            return null;
        }
    }
