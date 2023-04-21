    public void testPostRedirectSeeOther() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_SEE_OTHER));
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
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals("GET", reqWrapper.getRequestLine().getMethod());
    }
