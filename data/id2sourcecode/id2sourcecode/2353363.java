    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        try {
            String mySqlDate = rs.getString(names[0]);
            String date = mySqlDate;
            try {
                if (mySqlDate == null || mySqlDate.length() <= 0 || mySqlDate.equals("0000-00-00")) {
                    return null;
                }
                __formatter.setLenient(false);
                java.util.Date date1 = __inputDateFormat.parse(mySqlDate);
                date = __formatter.format(date1);
            } catch (ParseException de) {
            } catch (NumberFormatException de) {
            }
            return date;
        } catch (SQLException x) {
            if ("S1009".equals(x.getSQLState())) return null;
            throw x;
        }
    }
