    @Override
    public YearOfStudy store(YearOfStudy obj) throws InsertException, DBConnectionException, XmlIOException {
        String query;
        if (obj.getDepartment().getId() == null) {
            throw new InsertException("Missing Department FK");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getName());
            values.add(obj.getDescription());
            values.add(obj.getDurationSession());
            values.add(obj.getNbSessionsAM());
            values.add(obj.getNbSessionPM());
            values.add(obj.getDepartment().getId());
            query = new InsertQuery(TABLE_NAME, values).toString();
            try {
                stmt.executeUpdate(query);
                Criteria critWhere = new Criteria();
                critWhere.addCriterion("DEPARTMENT_ID", obj.getDepartment().getId());
                critWhere.addCriterion("YEAR_STUDY_NAME", obj.getName());
                List<SQLWord> listSelect = new ArrayList<SQLWord>();
                listSelect.add(new SQLWord("YEAR_STUDY_ID"));
                ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, listSelect, critWhere).toString());
                if (result != null) {
                    while (result.next()) obj.setId(result.getInt(1));
                } else {
                    throw new SelectException(TABLE_NAME + " Can't retrieve record");
                }
                stmt.getConnection().commit();
                stmt.close();
            } catch (SQLException e) {
                System.out.println(TABLE_NAME + " Store problem");
                e.printStackTrace();
                try {
                    stmt.getConnection().rollback();
                } catch (SQLException e1) {
                    throw new DBConnectionException("Rollback Exception :", e1);
                }
                throw new InsertException(TABLE_NAME + " Insert Exception :", e);
            }
        }
        return obj;
    }
