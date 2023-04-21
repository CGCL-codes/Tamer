    public static String getNumToDateCnvr(int srcNumber) {
        String pattern = null;
        String cnvrStr = null;
        String srcStr = String.valueOf(srcNumber);
        if (srcStr.length() != 8 && srcStr.length() != 14) {
            throw new IllegalArgumentException("Invalid Number: " + srcStr + " Length=" + srcStr.trim().length());
        }
        if (srcStr.length() == 8) {
            pattern = "yyyyMMdd";
        } else if (srcStr.length() == 14) {
            pattern = "yyyyMMddhhmmss";
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern, Locale.KOREA);
        Date cnvrDate = null;
        try {
            cnvrDate = dateFormatter.parse(srcStr);
        } catch (ParseException e) {
            Logger.getLogger(EgovStringUtil.class).debug(e);
        }
        cnvrStr = String.format("%1$tY-%1$tm-%1$td", cnvrDate);
        return cnvrStr;
    }
