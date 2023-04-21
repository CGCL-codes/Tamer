    public void testBasicAuthenticationSuccessOnRepeatablePost() throws Exception {
        localServer.register("*", new AuthHandler());
        localServer.start();
        TestCredentialsProvider credsProvider = new TestCredentialsProvider(new UsernamePasswordCredentials("test", "test"));
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.setCredentialsProvider(credsProvider);
        HttpPost httppost = new HttpPost("/");
        httppost.setEntity(new StringEntity("some important stuff", HTTP.ISO_8859_1));
        HttpResponse response = httpclient.execute(getServerHttp(), httppost);
        HttpEntity entity = response.getEntity();
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertNotNull(entity);
        entity.consumeContent();
        AuthScope authscope = credsProvider.getAuthScope();
        assertNotNull(authscope);
        assertEquals("test realm", authscope.getRealm());
    }
