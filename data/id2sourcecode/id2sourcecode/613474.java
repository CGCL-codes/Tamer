    private String getNextDay(String day) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(day);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return sdf.format(cal.getTime());
    }
