    public void testClientThrowsIntoDataHandlerIncomingData() throws Exception {
        setNumberOfExpectedWarnings(1);
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of correct channel type", c, client1.getClass());
        mockHandler.addBehavior("incomingData", new ThrowAndClone());
        log.info("class name" + client1.getClass());
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        client1.registerForReads((DataListener) mockHandler);
        mockConnect.expect("connected");
        TCPChannel svrChan = expectServerChannel(mockServer, c);
        verifyDataPassing(svrChan);
        verifyTearDown();
    }
