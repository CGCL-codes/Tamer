    @Test
    public void testAdvanced() throws ParseException {
        final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        final String[] dates = { "2009-04-01", "2009-04-02", "2009-04-03" };
        final String[] times = { "00:00", "12:00" };
        final int dateIndex = 1;
        final int timeIndex = 3;
        final int arrSz = 4;
        final String[] fields = new String[arrSz];
        final TimeStampParser p = new TimeStampParser(TimeZone.getDefault());
        p.addColumn(dateIndex, "yyyy-MM-dd");
        p.addColumn(timeIndex, "HH:mm");
        for (int i = 0; i < dates.length; i++) {
            fields[dateIndex] = dates[i];
            for (int j = 0; j < times.length; j++) {
                fields[timeIndex] = times[j];
                assertEquals("parsing failed.", dates[i] + "T" + times[j], fmt.format(((TimeStamp) p.parse(fields)).getAsDate()));
            }
        }
    }
