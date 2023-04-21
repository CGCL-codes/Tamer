    @Override
    public Department store(Department obj) throws InsertException, DBConnectionException, XmlIOException {
        Department toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getName());
        values.add(obj.getDescription());
        values.add(obj.getAcronym());
        try {
            stmt.executeUpdate(new InsertQuery(DepartmentDAO.TABLE_NAME, values).toString());
            toReturn = findByAcronym(obj.getAcronym());
            if (toReturn != null) toReturn.setYear_of_study(obj.getYearOfStudyList()); else {
                throw new SelectException(TABLE_NAME + " Can't retieve record");
            }
            stmt.getConnection().commit();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new InsertException(TABLE_NAME + " Insert Exception :", e);
        }
        return toReturn;
    }
