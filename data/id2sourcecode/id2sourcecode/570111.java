    public static String dateAdd2(String inputFormat, String inputDate, String durationFormat, String duration, String outputFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_1 = null;
        try {
            currentTime_1 = formatter.parse(inputDate, pos);
        } catch (Exception ex) {
            lf.warning(inputDate + " format does not match " + inputFormat, "XAFunctoids", "dateAdd2");
            return (inputDate + " format does not match " + inputFormat);
        }
        long inputTime = currentTime_1.getTime();
        SimpleDateFormat formatter2 = new SimpleDateFormat(durationFormat);
        ParsePosition pos2 = new ParsePosition(0);
        Date currentTime_2 = null;
        try {
            currentTime_2 = formatter2.parse(duration, pos2);
        } catch (Exception ex) {
            lf.warning(inputDate + " format does not match " + inputFormat, "XAFunctoids", "dateAdd2");
            return (inputDate + " format does not match " + inputFormat);
        }
        long duration2 = currentTime_2.getTime();
        long resultantTime = inputTime + duration2;
        Date resultantDate = new Date(resultantTime);
        SimpleDateFormat formatter3 = null;
        try {
            formatter3 = new SimpleDateFormat(outputFormat);
        } catch (Exception ex1) {
            lf.warning(outputFormat + " does not confirm to date specifications ", "XAFunctoids", "dateAdd2");
            return (outputFormat + " does not confirm to date specifications ");
        }
        String dateString = formatter3.format(resultantDate);
        return dateString;
    }
