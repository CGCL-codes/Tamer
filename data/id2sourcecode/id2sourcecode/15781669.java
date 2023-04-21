    public boolean exportSubset(String user, int subsetId, int namespaceId, QuerySession qs, DTSPermission permit) throws SQLException, DTSValidationException, PermissionException {
        checkPermission(permit, String.valueOf(namespaceId));
        boolean exported = false;
        PreparedStatement exportSubsetNspStmt = null;
        if (!checkNamespacePresent(namespaceId)) {
            throw new DTSValidationException("Namespace [" + namespaceId + "] not present in the knowledgebase for the subset [ " + subsetId + "] to be exported to that namespace. Please create one and retry exporting subset.");
        }
        String assocSeqName = this.subsetDAO.getStatement(SubsetDb.TABLE_KEY, GET_ASSOC_SEQ_NAME);
        int origTransLevel = Utilities.beginTransaction(this.conn);
        long beg = System.currentTimeMillis();
        try {
            HashMap assocMap = new HashMap(100);
            int count = 0;
            int assocId = 1;
            long assocGid = GidGenerator.getGID(namespaceId, assocId);
            deleteNamespaceRelated(namespaceId, assocGid, qs);
            qs.check();
            qs.setStatus("Fetching Subset Concepts ...");
            DTSTransferObject[] subsetCons = this.getSubsetConcepts(subsetId, qs);
            qs.check();
            String exportNspQuery = this.subsetDAO.getStatement(SubsetDb.TABLE_KEY, EXPORT_SUBSET_NSP_STMT);
            exportSubsetNspStmt = this.conn.prepareStatement(exportNspQuery);
            int smallBatchSize = 1000;
            if (SQL.getConnType(conn).equals(SQL.ORACLE)) {
                ((oracle.jdbc.driver.OraclePreparedStatement) exportSubsetNspStmt).setExecuteBatch(smallBatchSize);
            }
            addSubsetAssocType(namespaceId, assocId, assocGid);
            qs.check();
            qs.setStatus("Exporting Subset Concepts to Namespace ...");
            qs.startExecute(exportSubsetNspStmt);
            for (int i = 0; i < subsetCons.length; i++) {
                DTSTransferObject dt = subsetCons[i];
                int origConNamespaceId = dt.getNamespaceId();
                long origConceptGid = GidGenerator.getGID(origConNamespaceId, dt.getId());
                int conceptId = i + 1;
                long conceptGid = GidGenerator.getGID(namespaceId, conceptId);
                String conceptCode = "C" + conceptId;
                exportSubsetNspStmt.setLong(1, conceptGid);
                exportSubsetNspStmt.setInt(2, conceptId);
                exportSubsetNspStmt.setString(3, conceptCode);
                exportSubsetNspStmt.setString(4, dt.getName());
                exportSubsetNspStmt.setInt(5, namespaceId);
                exportSubsetNspStmt.setString(6, dt.getName().toUpperCase());
                exportSubsetNspStmt.executeUpdate();
                if (assocGid > 0) {
                    Long fromCon = new Long(conceptGid);
                    if (assocMap.containsKey(fromCon)) {
                        Vector toConVec = (Vector) assocMap.get(fromCon);
                        toConVec.addElement(new Long(origConceptGid));
                        assocMap.put(fromCon, toConVec);
                    } else {
                        Vector toConVec = new Vector(2);
                        toConVec.addElement(new Long(origConceptGid));
                        assocMap.put(fromCon, toConVec);
                    }
                }
                if ((++count % 1000) == 0) {
                    Categories.dataDb().debug("Exported [" + count + "] concepts to namespace [" + namespaceId + "]  for subset_id [" + subsetId + "]...");
                }
            }
            Categories.dataDb().debug("Exported successfully [" + count + "] concepts to namespace [" + namespaceId + "]  for subset_id [" + subsetId + "].");
            qs.endExecute();
            qs.check();
            this.conn.commit();
            qs.setStatus("Adding Concept Associations ...");
            this.addAssociations(assocMap, assocGid, assocSeqName, qs);
            qs.check();
            this.conn.commit();
            qs.setStatus("Exported [" + count + "] concepts to namespace [" + namespaceId + "]");
            exported = true;
            long end = System.currentTimeMillis();
            Categories.dataDb().debug("Exported [" + count + "] concepts to namespace [" + namespaceId + "] done in " + (end - beg) / 1000 + " secs");
            return exported;
        } catch (SQLException sqle) {
            Categories.dataDb().error("Problem exporting subset_id [" + subsetId + "] to namespace [" + namespaceId + "] : " + sqle.getMessage());
            this.conn.rollback();
            throw sqle;
        } finally {
            Utilities.endTransaction(this.conn, origTransLevel);
            if (exportSubsetNspStmt != null) {
                exportSubsetNspStmt.close();
            }
        }
    }
