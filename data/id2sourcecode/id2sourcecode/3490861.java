    @Override
    public void update(Subject obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("TEACHING_UNIT_ID", obj.getTeachingUnit().getId());
        newCrit.addCriterion("SUBJECT_NAME", obj.getName());
        newCrit.addCriterion("SUBJECT_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("SUBJECT_COEFFICIENT", obj.getCoeff());
        newCrit.addCriterion("SUBJECT_ALIAS", obj.getAlias());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("SUBJECT_ID", obj.getId());
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
