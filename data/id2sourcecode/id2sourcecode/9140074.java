    @Test
    @JUnitHttpServer(port = 9162, webapps = { @Webapp(context = "/testContext", path = "src/test/resources/test-webapp") })
    public void testWebapp() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest method = new HttpGet("http://localhost:9162/testContext/index.html");
        HttpResponse response = client.execute(method);
        String responseString = EntityUtils.toString(response.getEntity());
        LogUtils.debugf(this, "got response:\n%s", responseString);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseString.contains("This is a webapp."));
    }
