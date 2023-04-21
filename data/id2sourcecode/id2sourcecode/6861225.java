    public void testReadLineString() throws Exception {
        assertEquals("LINESTRING (10 10, 20 20, 30 40)", writer.write(reader.read("LINESTRING (10 10, 20 20, 30 40)")));
        assertEquals("LINESTRING EMPTY", writer.write(reader.read("LINESTRING EMPTY")));
    }
