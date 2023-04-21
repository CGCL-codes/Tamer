    @Test
    public void testActivateSslOnConnect4() throws Exception {
        System.out.println("testActivateSslOnConnect4...");
        IServer sslTestServer = new Server(0, new OnConnectSSLHandler4(), SSLTestContextFactory.getSSLContext(), false);
        ConnectionUtils.start(sslTestServer);
        SocketFactory socketFactory = SSLTestContextFactory.getSSLContext().getSocketFactory();
        LOG.info("creating socket");
        Socket socket = socketFactory.createSocket("localhost", sslTestServer.getLocalPort());
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        LOG.info("reading greeting");
        String greeting = lnr.readLine();
        Assert.assertEquals(greeting, GREETING);
        LOG.info("start write-read loop");
        for (int i = 0; i < 3; i++) {
            String req = "hello how are how sdfsfdsf sf sdf sf s sf sdf " + i;
            pw.write(req + DELIMITER);
            pw.flush();
            String res = lnr.readLine();
            if (!req.equals(res)) {
                System.out.println("response : " + res + " is not equals request: " + req);
                Assert.fail("request != response");
            }
        }
        lnr.close();
        pw.close();
        socket.close();
        sslTestServer.close();
    }
