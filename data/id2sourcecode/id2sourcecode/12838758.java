    public boolean unlink(Concern concern, String componentHandle, EdgeKind linkType) {
        List<Object> params = new ArrayList<Object>();
        params.add(concern.getId());
        params.add(componentHandle);
        params.add(getEdgeKindId(linkType));
        try {
            PreparedStatement statement = createPreparedStatement(REMOVE_CONCERN_COMPONENT_EDGE, params);
            int numUnlinked = statement.executeUpdate();
            statement.close();
            flushLinkCache(concern.getId(), linkType);
            con.commit();
            return numUnlinked != 0;
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return false;
        }
    }
