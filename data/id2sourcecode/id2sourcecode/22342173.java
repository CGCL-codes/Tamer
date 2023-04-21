    @Test
    public void testTransferWriter() throws IOException {
        int len = 4096 + 4096 + 1;
        StringBuilder builder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            builder.append((char) i);
        }
        String s = builder.toString();
        StringReader reader = new StringReader(s);
        StringWriter writer = new StringWriter();
        long bytes = getStreamUtil().transfer(reader, writer, true);
        assertEquals(len, bytes);
        assertEquals(s, writer.toString());
    }
