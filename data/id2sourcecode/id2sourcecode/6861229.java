    public void testReadMultiLineString() throws Exception {
        assertEquals("MULTILINESTRING ((10 10, 20 20), (15 15, 30 15))", writer.write(reader.read("MULTILINESTRING ((10 10, 20 20), (15 15, 30 15))")));
        assertEquals("MULTILINESTRING EMPTY", writer.write(reader.read("MULTILINESTRING EMPTY")));
    }
