    @Override
    public SessionType store(SessionType obj) throws InsertException, DBConnectionException, XmlIOException {
        SessionType toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getName());
        values.add(obj.getEquivTuto());
        values.add(obj.getAcronym());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            toReturn = findByAcronym(obj.getAcronym());
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
