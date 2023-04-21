    @Override
    public void delete(YearOfStudy obj) throws DeleteException, DBConnectionException, XmlIOException {
        String query;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria critDel = new Criteria();
        critDel.addCriterion("YEAR_STUDY_ID", obj.getId());
        query = new DeleteQuery(YearOfStudyDAO.TABLE_NAME, critDel).toString();
        try {
            stmt.executeUpdate(query);
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
            }
            throw new DeleteException(TABLE_NAME + " Deletion exception :", e);
        }
    }
