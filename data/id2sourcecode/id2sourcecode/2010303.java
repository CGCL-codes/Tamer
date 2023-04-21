    public void testGetWeekOfYear() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String start = "01/01/2002";
        GregorianCalendar walker = new GregorianCalendar();
        walker.setFirstDayOfWeek(Calendar.MONDAY);
        walker.setMinimalDaysInFirstWeek(4);
        walker.setTime(formatter.parse(start));
        eu.future.earth.gwt.emul.java.util.GregorianCalendar emul = new eu.future.earth.gwt.emul.java.util.GregorianCalendar();
        emul.setFirstDayOfWeek(Calendar.MONDAY);
        emul.setMinimalDaysInFirstWeek(4);
        for (int i = 0; i < 90000; i++) {
            emul.setTime(walker.getTime());
            int realWeek = walker.get(Calendar.WEEK_OF_YEAR);
            int emulWeek = emul.get(Calendar.WEEK_OF_YEAR);
            int week = WeekCounter.getWeekOfYear(walker.getTime(), Calendar.MONDAY, 4);
            assertEquals("Failed on " + formatter.format(walker.getTime()), realWeek, week);
            assertEquals("Failed on " + formatter.format(walker.getTime()), realWeek, emulWeek);
            walker.add(Calendar.DATE, 1);
        }
    }
