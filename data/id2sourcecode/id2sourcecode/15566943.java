    public void testCanNotReadResponseFromErrorStreamWhenNot5xxStatus() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.NoopServlet/").openConnection();
        assertNull(connection.getErrorStream());
    }
