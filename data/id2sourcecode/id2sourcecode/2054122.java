    public void testHindiLocale() {
        ValueFormatter dateFormatter = ValueFormatter.createFromPattern(ValueType.DATE, "MM | dd | yyyy", new ULocale("hi_IN"));
        DateValue dateValue = new DateValue(2009, 1, 2);
        String dateString = "०२ | ०२ | २००९";
        assertEquals(dateString, dateFormatter.format(dateValue));
        assertEquals(dateValue, dateFormatter.parse(dateString));
    }
