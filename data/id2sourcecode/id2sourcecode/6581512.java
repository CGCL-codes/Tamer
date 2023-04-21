    @Test
    public void testUnauthorizedList() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/list?version=5");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        assertEquals("Expecting request to be unauthorized.", HttpURLConnection.HTTP_UNAUTHORIZED, con.getResponseCode());
        final InputStream err = con.getErrorStream();
        assertNotNull("Expecting an error stream.", err);
    }
