    public void testUDPWithConnect() throws Exception {
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of secure channel", c, client1.getClass());
        client1.connect(remoteAddr);
        assertTrue("should be bound", client1.isBound());
        boolean isConnected = client1.isConnected();
        assertTrue("Client should be connected", isConnected);
        InetSocketAddress localAddr = client1.getLocalAddress();
        assertTrue("Port should not be 0", localAddr.getPort() != 0);
        DatagramChannel svrChan = mockServer.getUDPServerChannel();
        client1.registerForReads((DataListener) mockHandler);
        verifyDataPassing(svrChan);
        verifyTearDown();
        HandlerForTests.checkForWarnings();
    }
