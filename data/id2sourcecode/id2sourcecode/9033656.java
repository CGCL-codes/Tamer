    public void testProxyConnection_Not_Found_Response() throws Throwable {
        setUpStoreProperties();
        try {
            ServerSocket ss = new ServerSocket(0);
            TestHostnameVerifier hnv = new TestHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);
            URL url = new URL("https://localhost:" + ss.getLocalPort());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", ss.getLocalPort())));
            try {
                doInteraction(connection, ss, NOT_FOUND_CODE);
                fail("Expected exception was not thrown.");
            } catch (FileNotFoundException e) {
                if (DO_LOG) {
                    System.out.println("Expected exception was thrown: " + e.getMessage());
                }
            }
        } finally {
            tearDownStoreProperties();
        }
    }
