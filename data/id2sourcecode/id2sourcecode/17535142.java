    public void testDefaultHostAtClientLevel() throws Exception {
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new SimpleService());
        HttpHost target = new HttpHost("localhost", port);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.DEFAULT_HOST, target);
        String s = "/path";
        HttpGet httpget = new HttpGet(s);
        HttpResponse response = client.execute(httpget);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }
