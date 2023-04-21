    @Test
    public final void testParseParseRenderParse() throws Exception {
        StringReader reader1 = new StringReader(buildValidXMLDocument());
        AlignmentParser parser = INRIAFormatParser.getInstance();
        Alignment alignment1 = parser.parse(reader1);
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        AlignmentRenderer renderer = INRIAFormatRenderer.getInstance(pwriter);
        renderer.render(alignment1);
        reader1 = new StringReader(swriter.toString());
        parser = INRIAFormatParser.getInstance();
        Alignment alignment2 = parser.parse(reader1);
        assertEquals(alignment1, alignment2);
    }
