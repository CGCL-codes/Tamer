    public void testReadMultiPolygon() throws Exception {
        assertEquals("MULTIPOLYGON (((10 10, 10 20, 20 20, 20 15, 10 10)), ((60 60, 70 70, 80 60, 60 60)))", writer.write(reader.read("MULTIPOLYGON (((10 10, 10 20, 20 20, 20 15, 10 10)), ((60 60, 70 70, 80 60, 60 60)))")));
        assertEquals("MULTIPOLYGON EMPTY", writer.write(reader.read("MULTIPOLYGON EMPTY")));
    }
