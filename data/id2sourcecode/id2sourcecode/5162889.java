    public void testPostNoRedirect() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpPost httppost = new HttpPost("/oldlocation/");
        httppost.setEntity(new StringEntity("stuff"));
        HttpResponse response = client.execute(getServerHttp(), httppost, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals("POST", reqWrapper.getRequestLine().getMethod());
    }
