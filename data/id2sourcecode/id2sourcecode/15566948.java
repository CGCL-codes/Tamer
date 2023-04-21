    public void testManageHeaderWithMultipleValues() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.ExpectTestHeaderWithValuesTest1AndTest2/").openConnection();
        connection.addRequestProperty("Test", "Test1");
        connection.addRequestProperty("Test", "Test2");
        assertEquals(200, connection.getResponseCode());
    }
