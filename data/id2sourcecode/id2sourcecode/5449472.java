    public void testGetMissingClass() throws Exception {
        final SimpleRmiHttpServer server = new SimpleRmiHttpServer(TEST_PORT);
        try {
            final URL url = new URL(URL_BASE + "net/sf/openrds/SomeInexistent.class");
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            assertEquals("Wrong HTTP response code.", 404, con.getResponseCode());
        } finally {
            server.stopRunning(true);
        }
    }
