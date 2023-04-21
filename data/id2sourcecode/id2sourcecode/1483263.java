    @Test
    public void testDefault() throws ParseException {
        final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        final String[] timestamps = { "2009-04-01", "2009-04-02", "2009-04-03", "2009-04-04", "2009-04-05" };
        final int colIndex = 1;
        final int arrSz = 3;
        final String[] fields = new String[arrSz];
        final TimeStampParser p = new TimeStampParser(colIndex, fmt);
        for (int i = 0; i < timestamps.length; i++) {
            fields[colIndex] = timestamps[i];
            assertEquals("parsing failed.", timestamps[i], fmt.format(((TimeStamp) p.parse(fields)).getAsDate()));
        }
    }
