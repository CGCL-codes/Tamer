    @Override
    public ClassRoom store(ClassRoom obj) throws DBConnectionException, XmlIOException, InsertException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getName());
        values.add(obj.getCapacity());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            Criteria critWhere = new Criteria();
            critWhere.addCriterion("CLASSROOM_NAME", obj.getName());
            List<SQLWord> listSelect = new ArrayList<SQLWord>();
            listSelect.add(new SQLWord("CLASSROOM_ID"));
            ResultSet resultID = stmt.executeQuery(new SelectQuery(TABLE_NAME, listSelect, critWhere).toString());
            if (resultID != null) {
                resultID.next();
                obj.setId(resultID.getInt("CLASSROOM_ID"));
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
