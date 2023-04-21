    public void testReadWriteTwoObjects() throws IOException {
        Gson gson = new Gson();
        CharArrayWriter writer = new CharArrayWriter();
        BagOfPrimitives expectedOne = new BagOfPrimitives(1, 1, true, "one");
        writer.write(gson.toJson(expectedOne).toCharArray());
        BagOfPrimitives expectedTwo = new BagOfPrimitives(2, 2, false, "two");
        writer.write(gson.toJson(expectedTwo).toCharArray());
        CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        JsonStreamParser parser = new JsonStreamParser(reader);
        BagOfPrimitives actualOne = gson.fromJson(parser.next(), BagOfPrimitives.class);
        assertEquals("one", actualOne.stringValue);
        BagOfPrimitives actualTwo = gson.fromJson(parser.next(), BagOfPrimitives.class);
        assertEquals("two", actualTwo.stringValue);
        assertFalse(parser.hasNext());
    }
