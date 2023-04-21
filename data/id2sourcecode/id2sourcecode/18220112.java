    @Test
    public void testSecurityFailure() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        {
            HttpGet method = new HttpGet(generateURL("/secured"));
            HttpResponse response = client.execute(method);
            Assert.assertEquals(401, response.getStatusLine().getStatusCode());
            EntityUtils.consume(response.getEntity());
        }
        ClientExecutor executor = createAuthenticatingExecutor(client);
        {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("bill", "password");
            client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), credentials);
            ClientRequest request = new ClientRequest(generateURL("/secured/authorized"), executor);
            ClientResponse<String> response = request.get(String.class);
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            Assert.assertEquals("authorized", response.getEntity());
        }
        {
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("mo", "password");
            client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), credentials);
            ClientRequest request = new ClientRequest(generateURL("/secured/authorized"), executor);
            ClientResponse<?> response = request.get();
            Assert.assertEquals(HttpResponseCodes.SC_UNAUTHORIZED, response.getStatus());
            response.releaseConnection();
        }
    }
