    @Override
    public Teacher store(Teacher obj) throws InsertException, DBConnectionException, XmlIOException {
        String day;
        String month;
        String year;
        GregorianCalendar date;
        String query;
        Statement stmt = OracleJDBConnector.getInstance().getStatement();
        List<Object> values = new ArrayList<Object>();
        values.add(0);
        if (obj.getStatus() != null) {
            values.add(obj.getStatus().getId());
        } else {
            System.out.println("null");
            values.add("");
        }
        values.add(obj.getFirstName());
        values.add(obj.getLastName());
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
            values.add(day + "-" + month + "-" + year);
        }
        if (obj.getCity() != null) {
            values.add(obj.getCity());
        } else {
            values.add("");
        }
        if (obj.getPostalCode() != null) {
            values.add(obj.getPostalCode());
        } else {
            values.add(0);
        }
        if (obj.getAddress1() != null) {
            values.add(obj.getAddress1());
        } else {
            values.add("");
        }
        if (obj.getAddress2() != null) {
            values.add(obj.getAddress2());
        } else {
            values.add("");
        }
        if (obj.getTitle() != null) {
            values.add(obj.getTitle());
        } else {
            values.add("");
        }
        if (obj.getAlias() != null) {
            values.add(obj.getAlias());
        } else {
            values.add("");
        }
        System.out.println("before query");
        try {
            query = new InsertQuery(TeacherDAO.TABLE_NAME, values).toString();
            stmt.executeUpdate(query);
            Criteria critWhere = new Criteria();
            critWhere.addCriterion("TEACHER_FIRST_NAME", obj.getFirstName());
            critWhere.addCriterion("TEACHER_LAST_NAME", obj.getLastName());
            List<SQLWord> listSelect = new ArrayList<SQLWord>();
            listSelect.add(new SQLWord("TEACHER_ID"));
            ResultSet result = stmt.executeQuery(new SelectQuery(TeacherDAO.TABLE_NAME, listSelect, critWhere).toString());
            if (result != null) {
                while (result.next()) obj.setId(result.getInt(1));
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
                throw new DBConnectionException("Rollback Exception :", e1);
            }
            throw new InsertException(TABLE_NAME + " Insert Exception :", e);
        }
        return obj;
    }
