    public void testRegisterForReadsAfterConnect() throws Exception {
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of secure channel", c, client1.getClass());
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        mockConnect.expect("connected");
        boolean isConnected = client1.isConnected();
        assertTrue("Client should be connected", isConnected);
        InetSocketAddress localAddr = client1.getLocalAddress();
        assertTrue("Port should not be 0", localAddr.getPort() != 0);
        TCPChannel svrChan = ZNioFailureSuperclass.expectServerChannel(mockServer, c);
        client1.registerForReads((DataListener) mockHandler);
        verifyDataPassing(svrChan);
        verifyTearDown();
    }
