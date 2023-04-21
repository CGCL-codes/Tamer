    public void testReadPolygon() throws Exception {
        assertEquals("POLYGON ((10 10, 10 20, 20 20, 20 15, 10 10))", writer.write(reader.read("POLYGON ((10 10, 10 20, 20 20, 20 15, 10 10))")));
        assertEquals("POLYGON EMPTY", writer.write(reader.read("POLYGON EMPTY")));
    }
