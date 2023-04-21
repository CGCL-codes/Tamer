    @Test
    @JUnitHttpServer(port = 9162)
    public void testServer() throws HttpException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest method = new HttpGet("http://localhost:9162/test.html");
        HttpResponse response = client.execute(method);
        String responseString = EntityUtils.toString(response.getEntity());
        LogUtils.debugf(this, "got response:\n%s", responseString);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseString.contains("Purple monkey dishwasher."));
    }
