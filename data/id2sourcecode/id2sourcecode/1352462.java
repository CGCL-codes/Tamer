    public static Date convertDate(Date date, SimpleDateFormat dateFormat) {
        if (date != null) {
            try {
                return dateFormat.parse(dateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }
