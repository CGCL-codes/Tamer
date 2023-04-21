    @Override
    public Semester store(Semester obj) throws InsertException, DBConnectionException, XmlIOException {
        if (obj.getYearOfStudy().getId() == null || obj.getAcademicYear().getId() == null) throw new InsertException("Missing Foreign Key(s)");
        Semester toReturn = null;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        values.add(obj.getYearOfStudy().getId());
        values.add(obj.getDescription());
        values.add(obj.getAcademicYear().getId());
        values.add(obj.getName());
        values.add(obj.getLevel());
        values.add(obj.getDescription());
        values.add(obj.getSDate());
        values.add(obj.getEDate());
        try {
            stmt.executeUpdate(new InsertQuery(TABLE_NAME, values).toString());
            toReturn = findByValues(obj.getName(), obj.getYearOfStudy().getId(), obj.getAcademicYear().getId());
            if (toReturn != null) {
                toReturn.setAcademicYear(obj.getAcademicYear());
                toReturn.setYearOfStudy(obj.getYearOfStudy());
                toReturn.setTeachingUnit(obj.getTeachingUnitList());
            } else throw new SelectException(TABLE_NAME + " Can't retieve record");
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
