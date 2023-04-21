    public static Date getNowDate(String timeFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }
