    public void testReadLinearRing() throws Exception {
        try {
            reader.read("LINEARRING (10 10, 20 20, 30 40, 10 99)");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().indexOf("not form a closed linestring") > -1);
        }
        assertEquals("LINEARRING (10 10, 20 20, 30 40, 10 10)", writer.write(reader.read("LINEARRING (10 10, 20 20, 30 40, 10 10)")));
        assertEquals("LINEARRING EMPTY", writer.write(reader.read("LINEARRING EMPTY")));
    }
