    public Component createComponent(String name, ComponentKind componentKind, String handle, Integer beginLine, Integer beginColumn, Integer endLine, Integer endColumn, Integer numLines, ComponentDomain componentDomain) {
        PreparedStatement statement = null;
        Integer compSeqNum = null;
        try {
            compSeqNum = getNextSequenceNumber("COMPONENT_ID_SEQ", COMPONENT_TABLE);
            List<Object> params = new ArrayList<Object>();
            params.add(compSeqNum);
            params.add(name);
            params.add(getComponentKindId(componentKind));
            params.add(handle);
            params.add(beginLine);
            params.add(beginColumn);
            params.add(endLine);
            params.add(endColumn);
            params.add(numLines);
            statement = createPreparedStatement(COMPONENT_INSERT_SQL, params);
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            if (e.getMessage().startsWith("Violation of unique constraint")) {
                ProblemManager.reportException(e, "Program element handle '" + handle + "' is already present in database");
            } else {
                ProblemManager.reportException(e);
            }
            return null;
        }
        if (componentDomain == null) return getComponent(compSeqNum);
        assert compSeqNum != null;
        componentDomain.setId(compSeqNum);
        try {
            statement = createPreparedStatement(COMPONENT_DOMAIN_INSERT, componentDomain.getValuesAsList());
            statement.executeUpdate();
            statement.close();
            con.commit();
        } catch (SQLException e) {
            rollback();
            ProblemManager.reportException(e);
            return null;
        }
        return getComponent(compSeqNum);
    }
