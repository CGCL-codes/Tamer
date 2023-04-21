    public void testReadNaN() throws Exception {
        assertEquals("POINT (10 10)", writer.write(reader.read("POINT (10 10 NaN)")));
        assertEquals("POINT (10 10)", writer.write(reader.read("POINT (10 10 nan)")));
        assertEquals("POINT (10 10)", writer.write(reader.read("POINT (10 10 NAN)")));
    }
