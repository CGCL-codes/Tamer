    @Override
    public ClassSession store(ClassSession obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getPeriod() == null || obj.getElemEdSession() == null) {
            throw new InsertException(TABLE_NAME + " Missing Fields (Period or EES");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            if (obj.getClassRoom() != null) values.add(obj.getClassRoom().getId()); else values.add(null);
            values.add(obj.getElemEdSession().getId());
            values.add(obj.getPeriod().getDate());
            values.add(obj.getPeriod().getPosition());
            try {
                stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("EES_ID", obj.getElemEdSession().getId());
                critWhere.addCriterion("PERIOD_DATE", obj.getPeriod().getDate());
                critWhere.addCriterion("PERIOD_POSITION", obj.getPeriod().getPosition());
                if (obj.getClassRoom() != null) critWhere.addCriterion("CLASSROOM_ID", obj.getClassRoom().getId());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("CLASS_SESSION_ID"));
                ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, listSelect, critWhere).toString());
                if (result != null) {
                    while (result.next()) obj.setId(result.getInt("CLASS_SESSION_ID"));
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
