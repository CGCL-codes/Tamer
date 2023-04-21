    public boolean addChildConcern(Concern parent, Concern child) {
        concernToChildrenCache.put(parent.getId(), null);
        if (child.getParent() != null) {
            concernToChildrenCache.put(child.getParent().getId(), null);
        }
        assert !parent.equals(child);
        List<Object> params = new ArrayList<Object>();
        try {
            params.add(child.getId());
            params.add(this.getEdgeKindId(EdgeKind.CONTAINS));
            PreparedStatement statement = createPreparedStatement(REMOVE_CONCERN_EDGE_FOR_EDGE_KIND, params);
            statement.executeUpdate();
            statement.close();
            params.clear();
            params.add(parent.getId());
            params.add(child.getId());
            params.add(this.getEdgeKindId(EdgeKind.CONTAINS));
            statement = createPreparedStatement(CONCERN_EDGE_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
            return true;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }
