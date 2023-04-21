    public void testRequestURIRewriting() throws Exception {
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new SimpleService());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        String s = "http://localhost:" + port + "/path";
        HttpGet httpget = new HttpGet(s);
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertTrue(reqWrapper instanceof RequestWrapper);
        assertEquals("/path", reqWrapper.getRequestLine().getUri());
    }
