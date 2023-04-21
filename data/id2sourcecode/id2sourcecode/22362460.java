    @Override
    public void update(ClassSession obj) throws UpdateException, DBConnectionException, XmlIOException {
        if (obj.getElemEdSession().getId() == null || obj.getPeriod() == null) throw new UpdateException(TABLE_NAME + " Missing EES or Period Foreign Key");
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("EES_ID", obj.getElemEdSession().getId());
        newCrit.addCriterion("CLASSROOM_ID", obj.getClassRoom().getId());
        newCrit.addCriterion("PERIOD_DATE", obj.getPeriod().getDate());
        newCrit.addCriterion("PERIOD_POSITION", obj.getPeriod().getPosition());
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("CLASS_SESSION_ID", obj.getId());
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
