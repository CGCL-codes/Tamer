    @Test
    public void testUploadContentLengthRequired() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/databases/test1");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        assertEquals("Expecting missing Content-Length to be replied with 411 Length Required.", HttpURLConnection.HTTP_LENGTH_REQUIRED, con.getResponseCode());
    }
