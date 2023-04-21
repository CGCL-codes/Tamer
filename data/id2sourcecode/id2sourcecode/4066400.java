    @Override
    public void delete(ExtraordinaryClassSession obj) throws DeleteException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria critDel = new Criteria();
        critDel.addCriterion("EXTRAORDINARY_CLASSSESSION_ID", obj.getId());
        try {
            stmt.executeUpdate(new DeleteQuery(ExtraClassSessionDAO.TABLE_NAME, critDel).toString());
            stmt.getConnection().commit();
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
