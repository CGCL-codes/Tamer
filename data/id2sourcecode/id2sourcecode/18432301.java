    private void processMCResult(List concepts, int namespaceId) throws SQLException {
        int defaultLevel = conn.getTransactionIsolation();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_CONCEPT_PARENT");
        PreparedStatement addParentStmt = conn.prepareStatement(sql);
        sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_CONCEPT_CHILD");
        PreparedStatement addChildStmt = conn.prepareStatement(sql);
        sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_ROLE_BY_" + REF_BY_NUM);
        PreparedStatement addRoleStmt = conn.prepareStatement(sql);
        try {
            ((DTSClassifyGeneralDAO) getClassifyDAO()).getNamespaceLock(namespaceId, this.keepAliveStmt);
            cleanLocalNamespace(namespaceId);
            String seqName = getClassifyDAO().getStatement(TABLE_KEY, "GET_COMPLETE_ROLE_CON_SEQ");
            Set set = new HashSet();
            for (int i = 0; i < concepts.size(); i++) {
                Concept c = (Concept) concepts.get(i);
                String gid = c.getIdentifier();
                long conceptGID = ((Long) gidMap.get(gid)).longValue();
                List children = Arrays.asList(c.getInferredChildArray());
                addInferredConcepts(gidMap, set, conceptGID, gid, namespaceId, children, addChildStmt);
                List parents = Arrays.asList(c.getInferredParentArray());
                addInferredConcepts(gidMap, set, conceptGID, gid, namespaceId, parents, addParentStmt);
                addInferredRoles(gidMap, set, conceptGID, gid, namespaceId, Arrays.asList(c.getInferredRoleArray()), addRoleStmt, seqName);
            }
            sql = getClassifyDAO().getStatement(TABLE_KEY, "UPDATE_CLASSIFY_MONITOR");
            sql = getClassifyDAO().getStatement(sql, 1, "N");
            sql = getClassifyDAO().getStatement(sql, 2, sdf.format(new java.util.Date(System.currentTimeMillis())));
            sql += namespaceId;
            int result = keepAliveStmt.executeUpdate(sql);
            if (result == 0) {
                throw new SQLException("unable to update classification status");
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } catch (Exception e) {
            conn.rollback();
            throw toSQLException(e, "unable to process result: ");
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
            closeDBResources(new Statement[] { addParentStmt, addChildStmt, addRoleStmt });
        }
    }
