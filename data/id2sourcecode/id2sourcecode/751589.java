    public static void main(String[] args) {
        Date date = new Date();
        DateFormat df = DateFormat.getDateInstance();
        DateFormat df2 = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
        String dateString = df.format(date);
        System.out.println(dateString);
        String dateString2 = df2.format(date);
        System.out.println(dateString2);
        Date creationDate = null;
        try {
            creationDate = df.parse(dateString);
        } catch (java.text.ParseException ex) {
            System.out.println(ex);
        }
        System.out.println(creationDate);
    }
