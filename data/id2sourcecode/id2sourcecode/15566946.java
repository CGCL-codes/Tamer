    public void testMethodPostDelegatesToDoPost() throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL("servlet:soapdust.urlhandler.servlet.PostStatus201/").openConnection();
        connection.setRequestMethod("POST");
        assertEquals(201, connection.getResponseCode());
    }
