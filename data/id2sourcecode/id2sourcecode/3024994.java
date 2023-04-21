    @Test
    public void testCopy_readerToWriter_IO84() throws Exception {
        long size = (long) Integer.MAX_VALUE + (long) 1;
        Reader reader = new NullReaderTest(size);
        Writer writer = new NullWriterTest();
        assertEquals(-1, IOUtils.copy(reader, writer));
        reader.close();
        assertEquals("copyLarge()", size, IOUtils.copyLarge(reader, writer));
    }
