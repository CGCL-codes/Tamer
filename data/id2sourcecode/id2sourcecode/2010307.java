    public void testGetWeekOfYear31122007() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = "31/12/2007";
        Date date = formatter.parse(dateString);
        GregorianCalendar weekTest = new GregorianCalendar();
        weekTest.setFirstDayOfWeek(Calendar.MONDAY);
        weekTest.setTime(date);
        weekTest.setMinimalDaysInFirstWeek(4);
        int week = WeekCounter.getWeekOfYear(date, Calendar.MONDAY, 4);
        int real = weekTest.get(Calendar.WEEK_OF_YEAR);
        assertEquals("Failed on " + formatter.format(date), real, week);
        eu.future.earth.gwt.emul.java.util.GregorianCalendar emul = new eu.future.earth.gwt.emul.java.util.GregorianCalendar();
        emul.setFirstDayOfWeek(Calendar.MONDAY);
        emul.setTime(date);
        emul.setMinimalDaysInFirstWeek(4);
        int emulTest = emul.get(Calendar.WEEK_OF_YEAR);
        assertEquals("Failed on " + formatter.format(date), real, emulTest);
    }
