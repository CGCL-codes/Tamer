    public String getTimeOneYear(String tmp, String date) throws ParseException {
        SimpleDateFormat dateFm1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date myDate = dateFm1.parse(date);
        int tmp1 = Integer.parseInt(tmp);
        long myTime = (myDate.getTime() / 1000) + 365 * 60 * 60 * 24 * tmp1;
        myDate.setTime(myTime * 1000);
        String mDate = dateFm1.format(myDate);
        return mDate;
    }
