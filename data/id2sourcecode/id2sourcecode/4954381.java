    private ByteBuffer verifyDataPassing(TCPChannel svrChan) throws Exception {
        ByteBuffer b = ByteBuffer.allocate(10);
        helper.putString(b, "de");
        helper.doneFillingBuffer(b);
        int expectedWrote = b.remaining();
        int actualWrite = client1.write(b);
        assertEquals(expectedWrote, actualWrite);
        CalledMethod m = mockServer.expect(MockNIOServer.INCOMING_DATA);
        TCPChannel actualChannel = (TCPChannel) m.getAllParams()[0];
        Class c = Class.forName(getChannelImplName());
        assertEquals("should be correct type of channel", c, actualChannel.getClass());
        ByteBuffer actualBuf = (ByteBuffer) m.getAllParams()[1];
        String result = helper.readString(actualBuf, actualBuf.remaining());
        log.info("result len=" + result.length());
        assertEquals("de", result);
        b.rewind();
        svrChan.write(b);
        m = mockHandler.expect(MockDataHandler.INCOMING_DATA);
        actualBuf = (ByteBuffer) m.getAllParams()[1];
        log.info("buffer remain=" + actualBuf.remaining());
        result = helper.readString(actualBuf, actualBuf.remaining());
        log.info("---1st char=" + (result.substring(0, 1).equals("de".substring(0, 1))));
        log.info("---2nd char=" + (result.substring(1, 2).equals("de".substring(1, 2))));
        log.info("substring='" + result.substring(0, 1) + "'");
        log.info("len=" + "de".length() + "  2ndlen=" + result.length());
        log.info("'de'" + " actual='" + result + "'" + "  result=" + ("de".equals(result)));
        assertEquals("de", result);
        return b;
    }
