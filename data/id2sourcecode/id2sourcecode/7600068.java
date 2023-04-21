    @Override
    public TeachingUnit store(TeachingUnit obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getSemester().getId() == null) throw new InsertException("Missing Semester Foreign Key");
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getSemester().getId());
        values.add(obj.getName());
        values.add(obj.getDescription());
        values.add(obj.getNbECTS());
        values.add(obj.getMinScore());
        try {
            stmt.executeUpdate(new InsertQuery(TeachingUnitDAO.TABLE_NAME, values).toString());
            Criteria critWhere = new Criteria();
            critWhere.addCriterion("SEMESTER_ID", obj.getSemester().getId());
            critWhere.addCriterion("TEACHING_UNIT_NAME", obj.getName());
            List<SQLWord> listSelect = new ArrayList<SQLWord>();
            listSelect.add(new SQLWord("TEACHING_UNIT_ID"));
            ResultSet result = stmt.executeQuery(new SelectQuery(TeachingUnitDAO.TABLE_NAME, listSelect, critWhere).toString());
            if (result != null) {
                while (result.next()) {
                    obj.setId(result.getInt(1));
                }
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
        return obj;
    }
