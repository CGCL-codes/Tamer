    public void testClientThrowsIntoAcceptHandlerConnect() throws Exception {
        setNumberOfExpectedWarnings(1);
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of correct channel type", c, client1.getClass());
        String msg = "some exception message";
        IOException e = new IOException(msg);
        mockServer.addThrowException("connected", e);
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        client1.registerForReads((DataListener) mockHandler);
        mockConnect.expect("connected");
        TCPChannel svrChan = expectServerChannel(mockServer, c);
        verifyDataPassing(svrChan);
        verifyTearDown();
    }
