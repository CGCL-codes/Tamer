    public void testCloseSvrSocketBeforeChannelMgrShutdown() throws Exception {
        Class c = Class.forName(getChannelImplName());
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr);
        boolean isConnected = client1.isConnected();
        assertTrue("Client should be connected", isConnected);
        TCPChannel svrChan = ZNioFailureSuperclass.expectServerChannel(mockServer, c);
        client1.registerForReads((DataListener) mockHandler);
        verifyDataPassing(svrChan);
        svrChan.close();
        mockServer.stop();
        mockHandler.expect(MockNIOServer.FAR_END_CLOSED);
    }
