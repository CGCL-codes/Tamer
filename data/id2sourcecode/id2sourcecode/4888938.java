    @Test
    public void testFormatParse() {
        List<Alignment> alignmentList = createAlignmentList(AlFormatterTest.SOURCE_ARRAY, AlFormatterTest.TARGET_ARRAY);
        StringWriter writer = new StringWriter();
        Formatter formatter = new TmxFormatter(writer, SOURCE_LANGUAGE, TARGET_LANGUAGE);
        formatter.format(alignmentList);
        Reader reader = new StringReader(writer.toString());
        TmxParser parser = new TmxParser(reader, SOURCE_LANGUAGE, TARGET_LANGUAGE);
        List<Alignment> resultAlignmentList = parser.parse();
        assertAlignmentListEquals(EXPECTED_SOURCE_ARRAY, EXPECTED_TARGET_ARRAY, resultAlignmentList);
    }
