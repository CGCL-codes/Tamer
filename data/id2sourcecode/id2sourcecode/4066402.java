    @Override
    public ExtraordinaryClassSession store(ExtraordinaryClassSession obj) throws DBConnectionException, XmlIOException, InsertException {
        if (obj.getRoom() == null || obj.getTeacher() == null || obj.getPeriod().getDate() == null || obj.getPeriod().getPosition() == null) {
            throw new InsertException("Missing Field");
        } else {
            Statement stmt = OracleJDBConnector.getInstance().getStatement();
            List<Object> values = new ArrayList<Object>();
            values.add(0);
            values.add(obj.getRoom().getId());
            values.add(obj.getTeacher().getId());
            values.add(obj.getPeriod().getDate());
            values.add(obj.getPeriod().getPosition());
            try {
                stmt.executeUpdate(new InsertQuery(ExtraClassSessionDAO.TABLE_NAME, values).toString());
                stmt.getConnection().commit();
            } catch (SQLException e) {
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
