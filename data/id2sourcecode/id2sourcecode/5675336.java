    @Override
    public void update(Semester obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("SEMESTER_NAME", obj.getName());
        newCrit.addCriterion("SEMESTER_DESCRIPTION", obj.getDescription());
        newCrit.addCriterion("YEAR_STUDY_ID", obj.getYearOfStudy().getId());
        newCrit.addCriterion("ACADEMIC_YEAR_ID", obj.getAcademicYear().getId());
        newCrit.addCriterion("SEMESTER_LEVEL", obj.getLevel());
        newCrit.addCriterion("SEMESTER_STARTING_DATE", obj.getSDate());
        newCrit.addCriterion("SEMESTER_ENDING_DATE", obj.getEDate());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("SEMESTER_ID", obj.getId());
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
