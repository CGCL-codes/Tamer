    public void testParseComment() throws ParseException, IOException {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        out.println("# SOME DESCRIPTIVE TITLE.");
        out.println("# Copyright (C) YEAR Free Software Foundation, Inc.");
        out.println("# FIRST AUTHOR <EMAIL@ADDRESS>, YEAR.");
        out.println("#");
        out.println("#, fuzzy");
        String comment = writer.toString();
        out.println("msgid \"\"");
        out.println("msgstr \"\"");
        writer.flush();
        Reader reader = new StringReader(writer.toString());
        POParser parser = new POParser(callback);
        parser.parse(reader);
        assertEquals(callback.comment.trim(), comment.trim());
    }
