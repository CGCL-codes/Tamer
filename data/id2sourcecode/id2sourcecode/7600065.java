    @Override
    public void delete(TeachingUnit obj) throws DeleteException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria critDel = new Criteria();
        critDel.addCriterion("TEACHING_UNIT_ID", obj.getId());
        try {
            stmt.executeUpdate(new DeleteQuery(TeachingUnitDAO.TABLE_NAME, critDel).toString());
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new DeleteException(TABLE_NAME + " Deletion exception :", e);
        }
    }
