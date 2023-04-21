    @Test
    public void testCopy_readerToWriter() throws Exception {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStreamTest(in);
        Reader reader = new InputStreamReader(in, "US-ASCII");
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        YellOnFlushAndCloseOutputStreamTest out = new YellOnFlushAndCloseOutputStreamTest(baout, true, true);
        Writer writer = new OutputStreamWriter(baout, "US-ASCII");
        int count = IOUtils.copy(reader, writer);
        out.off();
        writer.flush();
        assertEquals("The number of characters returned by copy is wrong", inData.length, count);
        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }
