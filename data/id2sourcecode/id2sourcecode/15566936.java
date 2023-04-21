    public void testOpeningAServletURLReturnsAnHttpConnection() throws IOException {
        URLConnection connection = new URL("servlet:soapdust.urlhandler.servlet.NoopServlet/").openConnection();
        assertTrue(connection instanceof HttpURLConnection);
    }
