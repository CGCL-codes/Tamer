    protected void setUp() throws Exception {
        url = new URL("http://localhost:" + port + "/");
        uc = (HttpURLConnection) url.openConnection();
        port = Support_Jetty.startDefaultHttpServer();
    }
