    public static String formatDate(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            Date data = sdf.parse(date);
            cat.debug("[DateUtils] - formatDate: data formatada: " + sdf.format(data));
            setDateFormatter(format);
            return getDateFormatter().format(data);
        } catch (ParseException e) {
            cat.error("[DateUtils] - formatDate: ", e);
        }
        return "";
    }
