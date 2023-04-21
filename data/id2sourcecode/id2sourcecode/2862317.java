    public static String formatDate(String date, String format, String formatAtual) {
        try {
            setDateFormatter(formatAtual);
            Date data = getDateFormatter().parse(date);
            cat.debug("[DateUtils] - formatDate: data formatada: " + getDateFormatter().format(data));
            setDateFormatter(format);
            return getDateFormatter().format(data);
        } catch (ParseException e) {
            cat.error("[DateUtils] - formatDate: ", e);
        }
        return "";
    }
