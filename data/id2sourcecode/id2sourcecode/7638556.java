    @Test
    public void testUnsupportedMethod() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + testPort + "/list");
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Authorization", "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        con.setRequestMethod("PUT");
        assertEquals("Expecting method to be unsupported.", HttpURLConnection.HTTP_BAD_METHOD, con.getResponseCode());
        assertEquals("Expecting supported method GET to be listed in Allow header.", "GET", con.getHeaderField("Allow"));
    }
