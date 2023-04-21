    @Override
    public void update(TeachingUnit obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("SEMESTER_ID", obj.getSemester().getId());
        newCrit.addCriterion("TEACHING_UNIT_NAME", obj.getName());
        newCrit.addCriterion("TEACHING_UNIT_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("TEACHING_UNIT_NB_ECTS", obj.getNbECTS());
        newCrit.addCriterion("TEACHING_UNIT_MIN_SCORE", obj.getMinScore());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("TEACHING_UNIT_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(TeachingUnitDAO.TABLE_NAME, newCrit, critWhere).toString());
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
