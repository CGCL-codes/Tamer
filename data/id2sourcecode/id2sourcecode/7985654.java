    public void testReadWriteTwoStrings() throws IOException {
        Gson gson = new Gson();
        CharArrayWriter writer = new CharArrayWriter();
        writer.write(gson.toJson("one").toCharArray());
        writer.write(gson.toJson("two").toCharArray());
        CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        JsonStreamParser parser = new JsonStreamParser(reader);
        String actualOne = gson.fromJson(parser.next(), String.class);
        assertEquals("one", actualOne);
        String actualTwo = gson.fromJson(parser.next(), String.class);
        assertEquals("two", actualTwo);
    }
