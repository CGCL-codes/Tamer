    public void connectComponents(Component from, Component to, EdgeKind edgeKind) {
        assert (edgeKind != null);
        List<Object> params = new ArrayList<Object>();
        params.add(from.getId());
        params.add(to.getId());
        params.add(getEdgeKindId(edgeKind));
        try {
            PreparedStatement statement = createPreparedStatement(COMPONENT_EDGE_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e, "Failed to create '" + edgeKind.name() + "' component edge " + "from component " + from.getId() + " to " + to.getId() + ".");
        }
    }
