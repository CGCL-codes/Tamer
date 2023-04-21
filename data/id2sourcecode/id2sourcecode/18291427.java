    public void testNamespace() throws Exception {
        String url = "http://open.login.yahooapis.com/openid20/www.yahoo.com/xrds";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setDefaultUseCaches(false);
        con.setInstanceFollowRedirects(false);
        con.setDoInput(true);
        con.connect();
        SimpleHandler handler = new SimpleHandler();
        InputStreamReader reader = new InputStreamReader(con.getInputStream());
        try {
            XMLParser.parse(reader, handler, true);
            Node xrds = handler.getNode();
            assertEquals("xrds", xrds.getNamespace());
            Node xrd = xrds.getNode("xrd");
            Node service = xrd.getNode("service");
            assertTrue(0 != service.getNodes("type").size());
            assertEquals("xrds", service.getLastNode().getNamespace());
        } finally {
            reader.close();
            con.disconnect();
        }
    }
