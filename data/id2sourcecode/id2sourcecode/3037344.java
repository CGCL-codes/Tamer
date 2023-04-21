    @Override
    public AcademicYear store(AcademicYear obj) throws InsertException, DBConnectionException, XmlIOException {
        AcademicYear toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getName());
        try {
            stmt.executeUpdate(new InsertQuery(AcademicYearDAO.TABLE_NAME, values).toString());
            toReturn = findByName(obj.getName());
            if (toReturn == null) {
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
        return toReturn;
    }
