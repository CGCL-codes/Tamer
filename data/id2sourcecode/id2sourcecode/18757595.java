    @Override
    public void update(Teacher obj) throws UpdateException, DBConnectionException, XmlIOException {
        String day;
        String month;
        String year;
        GregorianCalendar date;
        String query;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        Criteria newCrit = new Criteria();
        newCrit.addCriterion("TEACHER_ID", obj.getId());
        if (obj.getStatus() != null) {
            newCrit.addCriterion("STATUS_ID", obj.getStatus().getId());
        }
        newCrit.addCriterion("TEACHER_FIRST_NAME", obj.getFirstName());
        newCrit.addCriterion("TEACHER_LAST_NAME", obj.getLastName());
        if (obj.getDob() != null) {
            date = obj.getDob();
            day = date.get(Calendar.DAY_OF_MONTH) + "";
            month = (date.get(Calendar.MONTH) + 1) + "";
            year = date.get(Calendar.YEAR) + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }
            if (year.length() < 4) {
                year = "0" + year;
                if (year.length() < 4) {
                    year = "0" + year;
                    if (year.length() < 4) {
                        year = "0" + year;
                    }
                }
            }
            newCrit.addCriterion("TEACHER_DOB", day + "-" + month + "-" + year);
        }
        if (obj.getCity() != null) {
            newCrit.addCriterion("TEACHER_CITY", obj.getCity());
        }
        if (obj.getPostalCode() != null) {
            newCrit.addCriterion("TEACHER_PC", obj.getPostalCode());
        }
        if (obj.getAddress1() != null) {
            newCrit.addCriterion("TEACHER_ADDRESS1", obj.getAddress1());
        }
        if (obj.getAddress2() != null) {
            newCrit.addCriterion("TEACHER_ADDRESS2", obj.getAddress2());
        }
        if (obj.getTitle() != null) {
            newCrit.addCriterion("TEACHER_TITLE", obj.getTitle());
        }
        if (obj.getAlias() != null) {
            newCrit.addCriterion("TEACHER_ALIAS", obj.getAlias());
        }
        Criteria critWhere = new Criteria();
        critWhere.addCriterion("TEACHER_ID", obj.getId());
        query = new UpdateQuery(TeacherDAO.TABLE_NAME, newCrit, critWhere).toString();
        try {
            stmt.executeUpdate(query);
            stmt.getConnection().commit();
        } catch (SQLException e) {
            try {
                stmt.getConnection().rollback();
            } catch (SQLException e1) {
                throw new DBConnectionException(TABLE_NAME + " Rollback Exception :", e1);
            }
            throw new UpdateException(TABLE_NAME + " Update exception", e);
        }
    }
