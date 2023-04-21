    public String processAssociation(Element root, DTSPermission permit) throws SQLException, XMLException, PermissionException {
        String operation = root.getAttribute(OPERATION);
        NodeList children = root.getChildNodes();
        int len = children.getLength();
        Node node = children.item(0);
        Entry entry = getEntry(node, permit);
        if (!entry.hasResult()) {
            return getFalseResult();
        }
        if (entry.mode.equals(SYNONYM) && operation.equals("ADD")) {
            return addSynonym(entry);
        }
        if (!entry.mode.equals(SYNONYM) && operation.equals("ADD")) {
            return addConceptTermAssociation(entry, children);
        }
        if (!entry.mode.equals(SYNONYM) && operation.equals("DELETE")) {
            return deleteConceptTermAssociation(entry);
        }
        String statement = getDAO().getStatement(TABLE_KEY, operation + "_" + entry.mode + "_ASSOCIATION");
        statement = getDAO().getStatement(statement, 1, String.valueOf(entry.fromGID));
        statement = getDAO().getStatement(statement, 2, String.valueOf(entry.associationGID));
        statement = getDAO().getStatement(statement, 3, String.valueOf(entry.toGID));
        statement = getDAO().getStatement(statement, 4, "'" + String.valueOf(entry.preferredFlag) + "'");
        WFPlugin wf = entry.getWF(this);
        char attrType = (entry.mode.equals(SYNONYM)) ? WFPlugin.ATTR_SYNONYM : WFPlugin.ATTR_ASSOCIATION;
        char editType = WFPlugin.EDIT_ADD;
        if (operation.equals("UPDATE")) {
            editType = WFPlugin.EDIT_UPDATE;
        } else if (operation.equals("DELETE")) {
            editType = WFPlugin.EDIT_DELETE;
        }
        conn.setAutoCommit(false);
        int defaultLevel = conn.getTransactionIsolation();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        int result = -1;
        try {
            result = keepAliveStmt.executeUpdate(statement);
            if (result != 1) {
                conn.rollback();
                return getFalseResult();
            }
            wf.update(entry.fromId, entry.fromNamespaceId, permit, attrType, editType);
            conn.commit();
            return getTrueResult();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
        }
    }
