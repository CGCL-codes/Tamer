    public static String getDateFrom(String dateFrom, int amount) {
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatYMD.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        return formatYMD.format(cal.getTime());
    }
