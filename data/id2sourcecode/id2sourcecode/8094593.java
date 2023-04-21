    private ByteBuffer verifyDataPassing(TCPChannel svrChan) throws Exception {
        ByteBuffer b = ByteBuffer.allocate(10);
        helper.putString(b, "de");
        helper.doneFillingBuffer(b);
        int expectedWrote = b.remaining();
        log.fine("***********************************************");
        int actualWrite = client1.write(b);
        assertEquals(expectedWrote, actualWrite);
        CalledMethod m = mockServer.expect(MockNIOServer.INCOMING_DATA);
        TCPChannel actualChannel = (TCPChannel) m.getAllParams()[0];
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be correct type of channel", c, actualChannel.getClass());
        ByteBuffer actualBuf = (ByteBuffer) m.getAllParams()[1];
        String result = helper.readString(actualBuf, actualBuf.remaining());
        assertEquals("de", result);
        b.rewind();
        svrChan.write(b);
        m = mockHandler.expect(MockDataHandler.INCOMING_DATA);
        actualBuf = (ByteBuffer) m.getAllParams()[1];
        result = helper.readString(actualBuf, actualBuf.remaining());
        assertEquals("de", result);
        return b;
    }
