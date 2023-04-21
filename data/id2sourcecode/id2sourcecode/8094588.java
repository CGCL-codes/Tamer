    public void testCloseSocketAfterChannelMgrShutdown() throws Exception {
        Class c = Class.forName(getChannelImplName());
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr);
        TCPChannel svrChan = ZNioFailureSuperclass.expectServerChannel(mockServer, c);
        client1.registerForReads((DataListener) mockHandler);
        verifyDataPassing(svrChan);
        mockServer.stop();
        svrChan.close();
        mockHandler.expect(MockNIOServer.FAR_END_CLOSED);
    }
