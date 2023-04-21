    public static String getYesterDay(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date strtodate = formatter.parse(strDate);
            long yesterLong = strtodate.getTime() - 24 * 60 * 60 * 1000;
            Date yester_date = new Date(yesterLong);
            return formatter.format(yester_date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
