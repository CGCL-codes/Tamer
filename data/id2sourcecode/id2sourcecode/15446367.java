    @Override
    public ElementaryEducationSession store(ElementaryEducationSession obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getTeacher() == null || obj.getSubjectModel() == null || obj.getStudentsGroup() == null) {
            throw new InsertException("Missing Field");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getTeacher().getId());
            values.add(obj.getSubjectModel().getId());
            values.add(obj.getStudentsGroup().getId());
            try {
                stmt.executeUpdate(new InsertQuery(EESDao.TABLE_NAME, values).toString());
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("TEACHER_ID", obj.getTeacher().getId());
                critWhere.addCriterion("SUBJECT_MODEL_ID", obj.getSubjectModel().getId());
                critWhere.addCriterion("STUDENT_GROUP_ID", obj.getStudentsGroup().getId());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("CLASS_SESSION_ID"));
                ResultSet result = stmt.executeQuery(new SelectQuery(EESDao.TABLE_NAME, listSelect, critWhere).toString());
                if (result != null) {
                    while (result.next()) obj.setId(result.getInt(1));
                } else {
                    throw new SelectException(TABLE_NAME + " Can't retieve record");
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
        }
        return obj;
    }
