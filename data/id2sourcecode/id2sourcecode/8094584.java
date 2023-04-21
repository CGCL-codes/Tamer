    public void xtestRegisterForReadsBeforeConnect() throws Exception {
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of correct channel type", c, client1.getClass());
        client1.bind(loopBackAnyPort);
        client1.registerForReads((DataListener) mockHandler);
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        mockConnect.expect("connected");
        boolean isConnected = client1.isConnected();
        assertTrue("Client should be connected", isConnected);
        TCPChannel svrChan = ZNioFailureSuperclass.expectServerChannel(mockServer, c);
        verifyDataPassing(svrChan);
        verifyTearDown();
    }
