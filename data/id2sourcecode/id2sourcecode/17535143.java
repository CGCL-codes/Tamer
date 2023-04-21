    public void testDefaultHostAtRequestLevel() throws Exception {
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new SimpleService());
        HttpHost target1 = new HttpHost("whatever", 80);
        HttpHost target2 = new HttpHost("localhost", port);
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(ClientPNames.DEFAULT_HOST, target1);
        String s = "/path";
        HttpGet httpget = new HttpGet(s);
        httpget.getParams().setParameter(ClientPNames.DEFAULT_HOST, target2);
        HttpResponse response = client.execute(httpget);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }
