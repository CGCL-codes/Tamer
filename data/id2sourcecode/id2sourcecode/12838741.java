    public int removeConcernAndChildren(Concern concern) {
        int numRemoved = 0;
        for (Concern child : concern.getChildren()) {
            numRemoved += removeConcernAndChildren(child);
        }
        try {
            if (concern.getParent() != null) concernToChildrenCache.remove(concern.getParent().getId());
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_CONCERN, concern.getId());
            statement.executeUpdate();
            statement.close();
            List<Object> params = new ArrayList<Object>();
            params.add(concern.getId());
            params.add(concern.getId());
            statement = createPreparedStatement(REMOVE_CONCERN_EDGE, params);
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_CONCERN, concern.getId());
            statement.executeUpdate();
            statement.close();
            statement = createPreparedStatement(REMOVE_CONCERN_DOMAIN, concern.getId());
            statement.executeUpdate();
            statement.close();
            con.commit();
            flushLinkCache(concern.getId());
            concernToChildrenCache.remove(concern.getId());
            numRemoved += 1;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
        }
        return numRemoved;
    }
