    public void testProxyAuthConnection() throws Throwable {
        setUpStoreProperties();
        try {
            ServerSocket ss = new ServerSocket(0);
            TestHostnameVerifier hnv = new TestHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);
            Authenticator.setDefault(new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("user", "password".toCharArray());
                }
            });
            URL url = new URL("https://requested.host:55555/requested.data");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", ss.getLocalPort())));
            SSLSocket peerSocket = (SSLSocket) doInteraction(connection, ss);
            checkConnectionStateParameters(connection, peerSocket);
            connection.connect();
        } finally {
            tearDownStoreProperties();
        }
    }
