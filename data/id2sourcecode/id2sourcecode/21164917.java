    public void xtestNoCancelledKeyException() throws Exception {
        HandlerForTests.setupLogging();
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be instance of correct channel type", client1.getClass(), c);
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr, (ConnectionCallback) mockConnect);
        client1.registerForReads((DataListener) mockHandler);
        String[] s = new String[2];
        s[0] = MockNIOServer.CONNECTED;
        s[1] = MockNIOServer.INCOMING_DATA;
        mockServer.expect(s);
        client1.registerForReads((DataListener) mockHandler);
        log.info("verify teardown");
        verifyTearDown();
        HandlerForTests.checkForWarnings();
    }
