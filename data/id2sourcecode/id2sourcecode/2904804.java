    public static final Date formatDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strToday = format.format(date);
        try {
            date = format.parse(strToday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
