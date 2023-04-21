    public void testProxyAuthConnectionFailed() throws Throwable {
        setUpStoreProperties();
        try {
            ServerSocket ss = new ServerSocket(0);
            TestHostnameVerifier hnv = new TestHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);
            URL url = new URL("https://requested.host:55555/requested.data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", ss.getLocalPort())));
            try {
                doInteraction(connection, ss, AUTHENTICATION_REQUIRED_CODE);
            } catch (IOException e) {
                if (DO_LOG) {
                    System.out.println("Got expected IOException: " + e.getMessage());
                }
            }
        } finally {
            tearDownStoreProperties();
        }
    }
