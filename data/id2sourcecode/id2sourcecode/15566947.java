    public void testManageHeaders() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.ExpectSoapActionHeaderWithValueTest/").openConnection();
        connection.addRequestProperty("SOAPAction", "Test");
        assertEquals(200, connection.getResponseCode());
    }
