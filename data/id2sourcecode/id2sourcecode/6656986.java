    @Override
    public void update(YearOfStudy obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("YEAR_STUDY_NAME", obj.getName());
        newCrit.addCriterion("YEAR_STUDY_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("YEAR_STUDY_DURATION_SESSION", obj.getDurationSession());
        newCrit.addCriterion("YEAR_STUDY_NB_SESSIONAM", obj.getNbSessionsAM());
        newCrit.addCriterion("YEAR_STUDY_NB_SESSIONPM", obj.getNbSessionPM());
        newCrit.addCriterion("YEAR_STUDY_DEPARTMENT_ID", obj.getDepartment().getId());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("YEAR_STUDY_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(YearOfStudyDAO.TABLE_NAME, newCrit, critWhere).toString());
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
