    @Override
    public GroupType store(GroupType obj) throws InsertException, DBConnectionException, XmlIOException {
        GroupType toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getLabel());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            toReturn = findByLabel(obj.getLabel());
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
        return toReturn;
    }
