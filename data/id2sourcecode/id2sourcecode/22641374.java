    public void testURLStream() throws IOException {
        String content = null;
        URL url = new URL("http://svn.apache.org/repos/asf/lucene/solr/trunk/");
        InputStream in = url.openStream();
        try {
            content = IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        assertTrue(content.length() > 10);
        ContentStreamBase stream = new ContentStreamBase.URLStream(url);
        assertEquals(content.length(), stream.getSize().intValue());
        in = stream.getStream();
        try {
            assertTrue(IOUtils.contentEquals(new ByteArrayInputStream(content.getBytes()), in));
        } finally {
            IOUtils.closeQuietly(in);
        }
        stream = new ContentStreamBase.URLStream(url);
        assertTrue(IOUtils.contentEquals(new StringReader(content), stream.getReader()));
    }
