    public void test_getInputStream() throws IOException {
        InputStream is = uc.getInputStream();
        byte[] ba = new byte[600];
        is.read(ba, 0, 600);
        is.close();
        String s = Util.toUTF8String(ba);
        assertTrue("Incorrect input stream read", s.indexOf("Hello OneHandler") > 0);
        URL url = new URL("http://localhost:" + port + "/fred-zz6.txt");
        is = url.openStream();
        assertTrue("available() less than 0", is.available() >= 0);
        is.close();
        Support_HttpServerSocket serversocket = new Support_HttpServerSocket();
        Support_URLConnector client = new Support_URLConnector();
        Support_HttpTests test = new Support_HttpTests(serversocket, client);
        test.runTests(this);
        serversocket = new Support_HttpServerSocket();
        Support_HttpServer server = new Support_HttpServer(serversocket, this);
        int p = Support_PortManager.getNextPort();
        server.startServer(p);
        serversocket = null;
        final String authTestUrl = "http://localhost:" + server.getPort() + Support_HttpServer.AUTHTEST;
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("test", "password".toCharArray());
            }
        });
        try {
            client.open(authTestUrl);
            is = client.getInputStream();
            int c = is.read();
            while (c > 0) {
                c = is.read();
            }
            c = is.read();
            is.close();
        } catch (FileNotFoundException e) {
            fail("Error performing authentication test: " + e);
        }
        final String invalidLocation = "/missingFile.htm";
        final String redirectTestUrl = "http://localhost:" + server.getPort() + Support_HttpServer.REDIRECTTEST;
        try {
            client.open(redirectTestUrl + "/" + Support_HttpServer.MOVED_PERM + "-" + invalidLocation);
            is = client.getInputStream();
            int c = is.read();
            while (c > 0) {
                c = is.read();
            }
            c = is.read();
            is.close();
            fail("Incorrect data returned on redirect to non-existent file.");
        } catch (FileNotFoundException e) {
        }
        server.stopServer();
    }
