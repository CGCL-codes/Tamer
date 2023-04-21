    public void testRelativeRedirect() throws Exception {
        int port = this.localServer.getServicePort();
        String host = this.localServer.getServiceHostName();
        this.localServer.register("*", new RelativeRedirectService());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        client.getParams().setBooleanParameter(ClientPNames.REJECT_RELATIVE_REDIRECT, false);
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/relativelocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }
