    public void testSetHostnameVerifier() throws Throwable {
        setUpStoreProperties();
        try {
            SSLServerSocket ss = (SSLServerSocket) getContext().getServerSocketFactory().createServerSocket(0);
            TestHostnameVerifier hnv = new TestHostnameVerifier();
            HttpsURLConnection.setDefaultHostnameVerifier(hnv);
            URL url = new URL("https://localhost:" + ss.getLocalPort());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            TestHostnameVerifier hnv_late = new TestHostnameVerifier();
            connection.setHostnameVerifier(hnv_late);
            SSLSocket peerSocket = (SSLSocket) doInteraction(connection, ss);
            assertTrue("Hostname verification was not done", hnv_late.verified);
            assertFalse("Hostname verification should not be done by this verifier", hnv.verified);
            checkConnectionStateParameters(connection, peerSocket);
            connection.connect();
        } finally {
            tearDownStoreProperties();
        }
    }
