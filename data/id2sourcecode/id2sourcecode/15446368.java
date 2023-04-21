    @Override
    public void update(ElementaryEducationSession obj) throws UpdateException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("TEACHER_ID", obj.getTeacher().getId());
        newCrit.addCriterion("SUBJECT_MODEL_ID", obj.getSubjectModel().getId());
        newCrit.addCriterion("STUDENT_GROUP_ID", obj.getStudentsGroup().getId());
        newCrit.addCriterion("EES_STATUTORY", obj.getStatutory());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("CLASS_SESSION_ID", obj.getId());
        try {
            stmt.executeUpdate(new UpdateQuery(ClassSessionDAO.TABLE_NAME, newCrit, critWhere).toString());
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
