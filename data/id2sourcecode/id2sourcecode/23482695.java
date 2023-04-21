    @Override
    public SubjectModel store(SubjectModel obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getSessionType().getId() == null || obj.getSubject().getId() == null || obj.getGroupType().getId() == null) {
            throw new InsertException(TABLE_NAME + " Missing Field");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getSessionType().getId());
            values.add(obj.getSubject().getId());
            values.add(obj.getGroupType().getId());
            values.add(obj.getNbHours());
            try {
                stmt.executeUpdate(new InsertQuery(SubjectModelDAO.TABLE_NAME, values).toString());
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("SESSION_TYPE_ID", obj.getSessionType().getId());
                critWhere.addCriterion("SUBJECT_ID", obj.getSubject().getId());
                critWhere.addCriterion("GROUP_TYPE_ID", obj.getGroupType().getId());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("SUBJECT_MODEL_ID"));
                ResultSet idSM = stmt.executeQuery(new SelectQuery(SubjectModelDAO.TABLE_NAME, listSelect, critWhere).toString());
                if (idSM != null) {
                    obj.setId(idSM.getInt("SUBJECT_MODEL_ID"));
                } else {
                    throw new SelectException("Can't retieve record");
                }
                stmt.getConnection().commit();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    stmt.getConnection().rollback();
                } catch (SQLException e1) {
                    throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
                }
                throw new InsertException(TABLE_NAME + " Insert Exception :", e);
            }
            return obj;
        }
    }
