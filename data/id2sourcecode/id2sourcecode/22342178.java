    @Test
    public void testTransferReaderAsyncStop() throws Exception {
        InputStreamReader reader = new InputStreamReader(DevZero.INSTANCE);
        StringWriter writer = new StringWriter();
        AsyncTransferrer transferrer = getStreamUtil().transferAsync(reader, writer, true);
        Thread.sleep(10);
        long size = writer.getBuffer().length();
        transferrer.cancel(true);
        assertTrue(size > 0);
        Thread.sleep(10);
        size = writer.getBuffer().length();
        Thread.sleep(10);
        assertEquals(size, writer.getBuffer().length());
    }
