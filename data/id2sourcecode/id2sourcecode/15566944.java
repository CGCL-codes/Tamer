    public void testOneCanWriteInAServletUrl() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.NoopServlet/").openConnection();
        connection.getOutputStream();
    }
