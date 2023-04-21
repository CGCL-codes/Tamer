    public void executeUpdate(String tableName, Vector vField, Vector vPkField) throws Exception {
        log.info("<<<<<< ApplicDBManager.executeUpdate >>>>>> Begin ");
        Connection conn = null;
        PreparedStatement pStmt = null;
        try {
            conn = getConnection();
            String listColDB = "";
            for (int i = 0; i < vField.size(); i++) {
                if (i != 0) listColDB += ", ";
                listColDB += ((Field) vField.elementAt(i)).getName() + " = ? ";
            }
            String query = " UPDATE " + tableName + "  SET " + listColDB;
            query += "  WHERE 1=1 ";
            for (int i = 0; i < vPkField.size(); i++) query += "    AND " + ((Field) vPkField.elementAt(i)).getName() + " = ? ";
            log.info("<<<<<< ApplicDBManager.executeUpdate - query = '" + query + "'");
            pStmt = conn.prepareStatement(query);
            int i = 1;
            for (int j = 0; j < vField.size(); j++) setPreparedStatementValue(pStmt, i++, (Field) vField.elementAt(j));
            for (int j = 0; j < vPkField.size(); j++) setPreparedStatementValue(pStmt, i++, (Field) vPkField.elementAt(j));
            pStmt.execute();
            conn.commit();
            log.info("<<<<<< ApplicDBManager.executeUpdate >>>>>>  End ");
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception e2) {
                throw new Exception("ApplicDBManager * executeUpdate Exception : " + e2.getMessage());
            }
            manageException(e, "executeUpdate");
        } finally {
            closeQueryObjects(null, pStmt);
            closeConnection(conn);
        }
    }
