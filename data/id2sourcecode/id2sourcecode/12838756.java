    public boolean link(Concern concern, Component component, EdgeKind linkType) {
        if (isLinked(concern, component, linkType)) return false;
        try {
            List<Object> params = new ArrayList<Object>();
            params.add(concern.getId());
            params.add(component.getId());
            params.add(getEdgeKindId(linkType));
            PreparedStatement preparedStatement = createPreparedStatement(CONCERN_COMPONENT_EDGE_SQL, params);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return true;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }
