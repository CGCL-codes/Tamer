    public void testSetDefaultSSLSocketFactory() throws Throwable {
        SSLContext ctx = getContext();
        SSLServerSocket ss = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(0);
        SSLSocketFactory socketFactory = (SSLSocketFactory) ctx.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
        assertSame("Default SSLSocketFactory differs from expected", socketFactory, HttpsURLConnection.getDefaultSSLSocketFactory());
        TestHostnameVerifier hnv = new TestHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
        URL url = new URL("https://localhost:" + ss.getLocalPort());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        TestHostnameVerifier hnv_late = new TestHostnameVerifier();
        HttpsURLConnection.setDefaultHostnameVerifier(hnv_late);
        SSLSocket peerSocket = (SSLSocket) doInteraction(connection, ss);
        checkConnectionStateParameters(connection, peerSocket);
        assertTrue("Hostname verification was not done", hnv.verified);
        assertFalse("Hostname verification should not be done by this verifier", hnv_late.verified);
        assertSame("Default SSLSocketFactory should be used", HttpsURLConnection.getDefaultSSLSocketFactory(), connection.getSSLSocketFactory());
        connection.connect();
    }
