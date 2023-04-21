    public void testStringNormalization() throws ParseException, IOException {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("#");
        out.println("msgid \"\"");
        out.println("msgstr \"\"");
        out.println("\"Project-Id-Version: PACKAGE VERSION\\n\"");
        out.println();
        out.println("#: gpl.xml:11 gpl.xml:30");
        out.println("msgid \"GNU \\nGeneral \\tPublic\\n \"License\"\"");
        out.println("msgstr \"test\"");
        writer.flush();
        Reader reader = new StringReader(writer.toString());
        POParser parser = new POParser(callback);
        parser.parse(reader);
        assertFalse(callback.entries.isEmpty());
        ParserEntry entry = callback.entries.get(0);
        assertEquals("GNU \nGeneral \tPublic\n \"License\"", entry.getMsgId());
    }
