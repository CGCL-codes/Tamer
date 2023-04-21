    public static Test suite() throws Exception {
        Locale loc = new Locale("ja");
        DateFormat sformat = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        String str = sformat.format(new Date(106, 0, 11));
        DateParser parser = new DateParser(loc);
        Date date = parser.parse(str);
        String str2 = sformat.format(date);
        TestSuite suite = new TestSuite();
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            TestSuite testLocale = new TestSuite(locale.toString());
            suite.addTest(testLocale);
            SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
            testLocale.addTest(new ParserTest(format, locale));
            format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            testLocale.addTest(new ParserTest(format, locale));
            format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.LONG, locale);
            testLocale.addTest(new ParserTest(format, locale));
            format = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.FULL, locale);
            testLocale.addTest(new ParserTest(format, locale));
        }
        return suite;
    }
