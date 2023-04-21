    public void testServletUrlRequestStatusCodeIsTheResquestStatusCodeOfTheServlet() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.Status500Servlet/").openConnection();
        assertEquals(500, connection.getResponseCode());
    }
