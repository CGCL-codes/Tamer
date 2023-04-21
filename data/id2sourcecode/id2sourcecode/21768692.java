    public void testReadWriteTwoObjects() throws Exception {
        Gson gson = new Gson();
        CharArrayWriter writer = new CharArrayWriter();
        BagOfPrimitives expectedOne = new BagOfPrimitives(1, 1, true, "one");
        writer.write(gson.toJson(expectedOne).toCharArray());
        BagOfPrimitives expectedTwo = new BagOfPrimitives(2, 2, false, "two");
        writer.write(gson.toJson(expectedTwo).toCharArray());
        CharArrayReader reader = new CharArrayReader(writer.toCharArray());
        JsonParserJavacc parser = new JsonParserJavacc(reader);
        JsonElement element1 = parser.parse();
        JsonElement element2 = parser.parse();
        BagOfPrimitives actualOne = gson.fromJson(element1, BagOfPrimitives.class);
        assertEquals("one", actualOne.stringValue);
        BagOfPrimitives actualTwo = gson.fromJson(element2, BagOfPrimitives.class);
        assertEquals("two", actualTwo.stringValue);
    }
