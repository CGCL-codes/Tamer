    public String getTimeOneSecond(String date) throws ParseException {
        SimpleDateFormat dateFm1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        java.util.Date myDate = dateFm1.parse(date);
        long myTime = (myDate.getTime() / 1000) + 1;
        myDate.setTime(myTime * 1000);
        String mDate = dateFm1.format(myDate);
        return mDate;
    }
