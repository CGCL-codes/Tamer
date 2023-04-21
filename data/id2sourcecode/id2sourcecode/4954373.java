    public void testClientThrowsIntoConnectCallback() throws Exception {
        setNumberOfExpectedWarnings(1);
        log.info("hello");
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of correct channel type", c, client1.getClass());
        log.info("class name" + client1.getClass().getName());
        String msg = "some exception message";
        IOException e = new IOException(msg);
        mockConnect.addThrowException("connected", e);
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        client1.registerForReads((DataListener) mockHandler);
        mockConnect.expect("connected");
        TCPChannel svrChan = expectServerChannel(mockServer, c);
        verifyDataPassing(svrChan);
        verifyTearDown();
    }
