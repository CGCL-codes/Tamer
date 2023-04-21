    public void testSetSSLSocketFactory() throws Throwable {
        SSLContext ctx = getContext();
        SSLServerSocket ss = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(0);
        TestHostnameVerifier hnv = new TestHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
        URL url = new URL("https://localhost:" + ss.getLocalPort());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        SSLSocketFactory socketFactory = (SSLSocketFactory) ctx.getSocketFactory();
        connection.setSSLSocketFactory(socketFactory);
        TestHostnameVerifier hnv_late = new TestHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hnv_late);
        SSLSocket peerSocket = (SSLSocket) doInteraction(connection, ss);
        checkConnectionStateParameters(connection, peerSocket);
        assertTrue("Hostname verification was not done", hnv.verified);
        assertFalse("Hostname verification should not be done by this verifier", hnv_late.verified);
        assertNotSame("Default SSLSocketFactory should not be used", HttpsURLConnection.getDefaultSSLSocketFactory(), connection.getSSLSocketFactory());
        assertSame("Result differs from expected", socketFactory, connection.getSSLSocketFactory());
        connection.connect();
    }
