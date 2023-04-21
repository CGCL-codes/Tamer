    public void testJcrURL() throws Exception {
        URL url = new URL(null, "jcr://exo:exo@ws/file1", new JcrURLStreamHandler(getRepositoryService()));
        assertNotNull("url not null", url);
        InputStream inputStream = url.openStream();
        assertNotNull("inputStream not null", inputStream);
        byte[] buf = new byte[inputStream.available()];
        inputStream.read(buf);
        String content = new String(buf);
        assertEquals("content", "this is the content", content);
        inputStream.close();
    }
