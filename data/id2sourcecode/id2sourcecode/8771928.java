    @Test
    public void testFormatParse() {
        List<Alignment> alignmentList = createAlignmentList(SOURCE_ARRAY, TARGET_ARRAY);
        StringWriter writer = new StringWriter();
        Formatter formatter = new AlFormatter(writer);
        formatter.format(alignmentList);
        Reader reader = new StringReader(writer.toString());
        AlParser parser = new AlParser(reader);
        List<Alignment> resultAlignmentList = parser.parse();
        assertAlignmentListEquals(SOURCE_ARRAY, TARGET_ARRAY, resultAlignmentList);
    }
