    public int removeComponentAndChildren(Component component) {
        this.linkMap = new EnumMap<EdgeKind, Map<Integer, Set<Component>>>(EdgeKind.class);
        this.handleToComponentCache.clear();
        this.componentToChildrenCache.clear();
        int numRemoved = 0;
        for (Component child : component.getChildren()) {
            numRemoved += removeComponentAndChildren(child);
        }
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_COMPONENT, component.getId());
            statement.executeUpdate();
            statement.close();
            List<Object> params = new ArrayList<Object>();
            params.add(component.getId());
            params.add(component.getId());
            statement = createPreparedStatement(REMOVE_COMPONENT_EDGE, params);
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_COMPONENT, component.getId());
            statement.executeUpdate();
            statement.close();
            con.commit();
            return 1;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return 0;
        }
    }
