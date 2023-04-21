    @Test
    @JUnitHttpServer(port = 9162, basicAuth = true, webapps = { @Webapp(context = "/testContext", path = "src/test/resources/test-webapp") })
    public void testBasicAuthFailure() throws Exception {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpUriRequest method = new HttpGet("http://localhost:9162/testContext/monkey");
        final CredentialsProvider cp = client.getCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "sucks");
        cp.setCredentials(new AuthScope("localhost", 9162), credentials);
        final HttpResponse response = client.execute(method);
        assertEquals(401, response.getStatusLine().getStatusCode());
    }
