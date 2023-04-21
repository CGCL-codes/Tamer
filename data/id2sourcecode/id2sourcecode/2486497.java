    public void testNativeJavaAccessorsAndMutators() throws DataTypeException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd HH:ss");
        Date date = format.parse("20100609 12:40");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        commonDT = new CommonDT();
        commonDT.setValue(cal);
        assertEquals("20100609", commonDT.getValue());
        commonDT = new CommonDT();
        commonDT.setValue(date);
        assertEquals("20100609", commonDT.getValue());
        commonDT = new CommonDT();
        commonDT.setValue("20100609");
        assertEquals("20100609 00:00", format.format(commonDT.getValueAsDate()));
        commonDT = new CommonDT();
        commonDT.setValue("20100609");
        assertEquals("20100609 00:00", format.format(commonDT.getValueAsCalendar().getTime()));
    }
