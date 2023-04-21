    @Test
    public void testUnauthorizedFileNotFound() throws IOException {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/thisdoesnotexist");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        assertEquals("Expecting request to be unauthorized.", HttpURLConnection.HTTP_UNAUTHORIZED, con.getResponseCode());
        final InputStream err = con.getErrorStream();
        assertNotNull("Expecting an error stream.", err);
    }
