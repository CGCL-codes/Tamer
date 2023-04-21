    @Override
    public void update(AcademicYear obj) throws DBConnectionException, XmlIOException, UpdateException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("ACADEMIC_YEAR_NAME", obj.getName());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("ACADEMIC_YEAR_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(TABLE_NAME, newCrit, critWhere).toString());
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
            }
            throw new UpdateException(TABLE_NAME + " Update exception", e);
        }
    }
