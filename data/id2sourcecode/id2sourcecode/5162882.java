    public void testBasicRedirect302() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_MOVED_TEMPORARILY));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }
