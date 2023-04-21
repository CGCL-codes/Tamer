    @Test
    public void testUploadContentNotImplemented() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/databases/test1");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        con.setRequestProperty("Content-Length", "12");
        con.setRequestProperty("Content-Foo", "foobar");
        assertEquals("Expecting Content-Foo header to be not implemenented.", HttpURLConnection.HTTP_NOT_IMPLEMENTED, con.getResponseCode());
    }
