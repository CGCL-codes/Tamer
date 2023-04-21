    public void testProxyAuthConnection_doOutput() throws Throwable {
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
            URL url = new URL("https://requested.host:55554/requested.data");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", ss.getLocalPort())));
            connection.setDoOutput(true);
            SSLSocket peerSocket = (SSLSocket) doInteraction(connection, ss, OK_CODE);
            checkConnectionStateParameters(connection, peerSocket);
        } finally {
            tearDownStoreProperties();
        }
    }
