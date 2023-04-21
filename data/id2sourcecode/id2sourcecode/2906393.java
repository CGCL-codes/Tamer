    public static SimpleDate getDate() {
        String df = "yyyy.MM.dd";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(df);
        return SimpleDate.parse(sdf.format(cal.getTime()));
    }
