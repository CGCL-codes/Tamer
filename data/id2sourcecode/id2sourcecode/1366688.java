    private static String getNameOrAbbrev(String in, String[] formatsIn, String formatOut) throws ParseException {
        for (int i = 0; i < formatsIn.length; i++) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(formatsIn[i], Locale.ENGLISH);
                dateFormat.setLenient(false);
                Date dt = dateFormat.parse(in);
                dateFormat.applyPattern(formatOut);
                return dateFormat.format(dt);
            } catch (ParseException pe) {
            }
        }
        return "";
    }
