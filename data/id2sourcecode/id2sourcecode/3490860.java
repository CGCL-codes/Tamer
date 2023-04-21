    @Override
    public Subject store(Subject obj) throws InsertException, DBConnectionException {
        if (obj.getTeachingUnit().getId() == null) {
            throw new InsertException("Missing Teaching Unit FK");
        } else {
            Statement stmt;
            try {
                stmt = OracleJDBConnector.getInstance().getStatement();
            } catch (XmlIOException e2) {
                e2.printStackTrace();
                throw new DBConnectionException("Unable to get statement", e2);
            }
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getTeachingUnit().getId());
            values.add(obj.getDescription());
            values.add(obj.getName());
            values.add(obj.getDescription());
            values.add(obj.getCoeff());
            values.add(obj.getAlias());
            try {
                stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("TEACHING_UNIT_ID", obj.getTeachingUnit().getId());
                critWhere.addCriterion("SUBJECT_NAME", obj.getName());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("SUBJECT_ID"));
                ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, listSelect, critWhere).toString());
                if (result != null) {
                    while (result.next()) obj.setId(result.getInt("SUBJECT_ID"));
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
