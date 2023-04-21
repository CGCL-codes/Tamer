    public void testParseHeader() throws ParseException, IOException {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("#");
        out.println("msgid \"\"");
        out.println("msgstr \"\"");
        out.println("\"Project-Id-Version: PACKAGE VERSION\\n\"");
        out.println("\"POT-Creation-Date: 2001-02-09 01:25+0100\\n\"");
        writer.flush();
        Reader reader = new StringReader(writer.toString());
        POParser parser = new POParser(callback);
        parser.parse(reader);
        assertEquals(2, callback.headers.size());
        assertTrue(callback.headers.containsKey("Project-Id-Version"));
        assertEquals("PACKAGE VERSION", callback.headers.get("Project-Id-Version"));
        assertTrue(callback.headers.containsKey("POT-Creation-Date"));
        assertEquals("2001-02-09 01:25+0100", callback.headers.get("POT-Creation-Date"));
    }
