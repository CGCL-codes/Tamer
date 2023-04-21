    public void testUnregisterReregisterForReads() throws Exception {
        Class c = Class.forName(getChannelImplName());
        client1.bind(loopBackAnyPort);
        client1.connect(svrAddr);
        TCPChannel svrChan = ZNioFailureSuperclass.expectServerChannel(mockServer, c);
        client1.registerForReads((DataListener) mockHandler);
        ByteBuffer b = verifyDataPassing(svrChan);
        client1.unregisterForReads();
        b.rewind();
        svrChan.write(b);
        Thread.sleep(5000);
        mockHandler.expect(MockObject.NONE);
        client1.registerForReads((DataListener) mockHandler);
        CalledMethod m = mockHandler.expect(MockNIOServer.INCOMING_DATA);
        ByteBuffer actualBuf = (ByteBuffer) m.getAllParams()[1];
        String result = helper.readString(actualBuf, actualBuf.remaining());
        assertEquals("de", result);
        verifyTearDown();
    }
