    @Test
    public void testUploadContentNotImplemented() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/databases/test1");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        con.setRequestProperty("Content-Length", "12");
        con.setRequestProperty("Content-Foo", "foobar");
        con.setDoOutput(true);
        final OutputStream out = con.getOutputStream();
        final byte[] b = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        out.write(b);
        out.close();
        assertEquals("Expecting Content-Foo header to be not implemented.", HttpURLConnection.HTTP_NOT_IMPLEMENTED, con.getResponseCode());
    }
