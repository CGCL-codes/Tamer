    public static java.util.Date sqlDateToutilDate(java.sql.Date sDate) throws ParseException {
        return (java.util.Date) utilDateFormatter.parse(utilDateFormatter.format(sDate));
    }
