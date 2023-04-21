    public void testServletUrlRequestStatusCodeDefaultsTo200() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.NoopServlet").openConnection();
        assertEquals(200, connection.getResponseCode());
    }
