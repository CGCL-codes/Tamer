    public String formatDateStr(String dt) throws ParseException {
        SimpleDateFormat localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = localTime.parse(dt);
        return localTime.format(date1);
    }
