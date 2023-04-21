    public void testWrittenDataIsSentToServlet() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.EchoServlet/").openConnection();
        OutputStream out = connection.getOutputStream();
        out.write(HandlerTest.TEST_DATA);
        out.flush();
        out.close();
        assertStreamContent(TEST_DATA, connection.getInputStream());
    }
