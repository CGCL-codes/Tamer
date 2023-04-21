    public void test_parse_gregorianCalendar_RDFLiteral_vice_versa() {
        RDFSLiteral literal = model.createRDFSLiteral("2008-06-21T20:22:03", model.getRDFSDatatypeByName("xsd:dateTime"));
        assertEquals("2008-06-21T20:22:03", literal.getString());
        GregorianCalendar calendar = new GregorianCalendar();
        Date date = null;
        SimpleDateFormat xmlFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = xmlFormat.parse(literal.getString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        assertEquals(2008, calendar.get(GregorianCalendar.YEAR));
        assertEquals(05, calendar.get(GregorianCalendar.MONTH));
        assertEquals(21, calendar.get(GregorianCalendar.DAY_OF_MONTH));
        assertEquals(20, calendar.get(GregorianCalendar.HOUR_OF_DAY));
        assertEquals(22, calendar.get(GregorianCalendar.MINUTE));
        assertEquals(03, calendar.get(GregorianCalendar.SECOND));
        assertEquals("2008-06-21T20:22:03", xmlFormat.format(calendar.getTime()));
    }
