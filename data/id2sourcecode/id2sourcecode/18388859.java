    @Override
    public Status store(Status obj) throws InsertException, DBConnectionException, XmlIOException {
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getName());
        values.add(obj.getNbHours());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            Criteria critWhere = new Criteria();
            critWhere.addCriterion("STATUS_NAME", obj.getName());
            List<SQLWord> listSelect = new ArrayList<SQLWord>();
            listSelect.add(new SQLWord("STATUS_ID"));
            ResultSet result = stmt.executeQuery(new SelectQuery(TABLE_NAME, listSelect, critWhere).toString());
            if (result != null) {
                while (result.next()) obj.setId(result.getInt("STATUS_ID"));
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
