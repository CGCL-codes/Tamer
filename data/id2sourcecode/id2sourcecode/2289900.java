    public static boolean isValidDate(final String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        if (!sdf.format(testDate).equals(date)) {
            return false;
        }
        return true;
    }
