    public void testReadMultiPoint() throws Exception {
        assertEquals("MULTIPOINT ((10 10), (20 20))", writer.write(reader.read("MULTIPOINT ((10 10), (20 20))")));
        assertEquals("MULTIPOINT EMPTY", writer.write(reader.read("MULTIPOINT EMPTY")));
    }
