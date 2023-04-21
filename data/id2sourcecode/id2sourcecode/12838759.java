    public int unlink(Concern concern, EdgeKind linkType) {
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_ALL_CONCERN_COMPONENT_EDGES_FOR_EDGE_KIND, concern.getId(), getEdgeKindId(linkType));
            int numUnlinked = statement.executeUpdate();
            statement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return numUnlinked;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return 0;
        }
    }
