    public void testReadPoint() throws Exception {
        assertEquals("POINT (10 10)", writer.write(reader.read("POINT (10 10)")));
        assertEquals("POINT EMPTY", writer.write(reader.read("POINT EMPTY")));
    }
