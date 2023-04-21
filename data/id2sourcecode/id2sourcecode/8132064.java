    public void testBasicAuthenticationNoCreds() throws Exception {
        localServer.register("*", new AuthHandler());
        localServer.start();
        TestCredentialsProvider credsProvider = new TestCredentialsProvider(null);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.setCredentialsProvider(credsProvider);
        HttpGet httpget = new HttpGet("/");
        HttpResponse response = httpclient.execute(getServerHttp(), httpget);
        HttpEntity entity = response.getEntity();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
        assertNotNull(entity);
        entity.consumeContent();
        AuthScope authscope = credsProvider.getAuthScope();
        assertNotNull(authscope);
        assertEquals("test realm", authscope.getRealm());
    }
