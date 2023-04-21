    @Override
    public void update(SubjectModel obj) throws UpdateException, DBConnectionException {
        Statement stmt;
        try {
            stmt = OracleJDBConnector.getInstance().getStatement();
        } catch (XmlIOException e2) {
            e2.printStackTrace();
            throw new DBConnectionException("Unable to Get Statement", e2);
        }
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("SUBJECT_MODEL_NB_HOURS", obj.getNbHours());
        newCrit.addCriterion("SUBJECT_ID", obj.getSubject().getId());
        newCrit.addCriterion("SESSION_TYPE_ID", obj.getSessionType().getId());
        newCrit.addCriterion("GROUP_TYPE_ID", obj.getGroupType().getId());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("SUBJECT_MODEL_ID", obj.getId());
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
