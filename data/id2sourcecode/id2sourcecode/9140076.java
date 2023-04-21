    @Test
    @JUnitHttpServer(port = 9162, basicAuth = true, webapps = { @Webapp(context = "/testContext", path = "src/test/resources/test-webapp") })
    public void testBasicAuthSuccess() throws Exception {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpUriRequest method = new HttpGet("http://localhost:9162/testContext/monkey");
        final CredentialsProvider cp = client.getCredentialsProvider();
        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "istrator");
        cp.setCredentials(new AuthScope("localhost", 9162), credentials);
        final HttpResponse response = client.execute(method);
        final String responseString = EntityUtils.toString(response.getEntity());
        LogUtils.debugf(this, "got response:\n%s", responseString);
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertTrue(responseString.contains("You are reading this from a servlet!"));
    }
