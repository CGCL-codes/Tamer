    public void testReadGeometryCollection() throws Exception {
        assertEquals("GEOMETRYCOLLECTION (POINT (10 10), POINT (30 30), LINESTRING (15 15, 20 20))", writer.write(reader.read("GEOMETRYCOLLECTION (POINT (10 10), POINT (30 30), LINESTRING (15 15, 20 20))")));
        assertEquals("GEOMETRYCOLLECTION (POINT (10 10), LINEARRING EMPTY, LINESTRING (15 15, 20 20))", writer.write(reader.read("GEOMETRYCOLLECTION (POINT (10 10), LINEARRING EMPTY, LINESTRING (15 15, 20 20))")));
        assertEquals("GEOMETRYCOLLECTION (POINT (10 10), LINEARRING (10 10, 20 20, 30 40, 10 10), LINESTRING (15 15, 20 20))", writer.write(reader.read("GEOMETRYCOLLECTION (POINT (10 10), LINEARRING (10 10, 20 20, 30 40, 10 10), LINESTRING (15 15, 20 20))")));
        assertEquals("GEOMETRYCOLLECTION EMPTY", writer.write(reader.read("GEOMETRYCOLLECTION EMPTY")));
    }
