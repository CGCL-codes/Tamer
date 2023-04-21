    @Override
    public void update(Department obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("DEPARTMENT_NAME", obj.getName());
        newCrit.addCriterion("DEPARTMENT_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("DEPARTMENT_ACRONYM", obj.getAcronym());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("DEPARTMENT_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(DepartmentDAO.TABLE_NAME, newCrit, critWhere).toString());
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new UpdateException(TABLE_NAME + " Update exception", e);
        }
    }
