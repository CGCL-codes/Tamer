    public void testBasicRedirect305() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_USE_PROXY));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_USE_PROXY, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
    }
