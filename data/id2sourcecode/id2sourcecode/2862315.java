    public static String compareDate(String pDate, String format) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        try {
            Date date = df.parse(pDate);
            cat.debug("[DateUtils] - compareDate: date = " + date.toString());
            String sNow = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
            Date now = df.parse(sNow);
            cat.debug("[DateUtils] - compareDate: now = " + now.toString());
            if (date.after(now)) {
                cat.debug("[DateUtils] - compareDate: 1");
                return "1";
            } else if (date.before(now)) {
                cat.debug("[DateUtils] - compareDate: -1");
                return "-1";
            } else {
                cat.debug("[DateUtils] - compareDate: 0");
                return "0";
            }
        } catch (ParseException e) {
            cat.error("[DateUtils] - compareDate: -2", e);
            return "-2";
        }
    }
