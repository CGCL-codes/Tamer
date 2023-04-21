    public void testDiscoOnBadResponseConnectLine() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("GNUTELLA CONNECT/0.6\r\n" + "RequestHeader: RequestValue\r\n" + "\r\n" + "HTTP/1.1 543 WHAT ARE YOU DOING?\r\n" + "ResponseHeader: ResponseValue\r\n" + "\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        Properties outProps = new Properties();
        outProps.put("OutHeader", "OutValue");
        StubHandshakeResponder responder = new StubHandshakeResponder(new StubHandshakeResponse(200, "OK!", outProps));
        StubHandshakeObserver observer = new StubHandshakeObserver();
        Handshaker shaker = new AsyncIncomingHandshaker(responder, socket, observer);
        shaker.shake();
        socket.exchange();
        assertFalse(observer.isNoGOK());
        assertTrue(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
        Map respondedTo = responder.getRespondedToProps();
        assertEquals(respondedTo.toString(), 1, respondedTo.size());
        assertEquals("RequestValue", respondedTo.get("RequestHeader"));
        assertFalse(responder.isOutgoing());
        HandshakeResponse read = shaker.getReadHeaders();
        assertEquals(1, read.props().size());
        assertEquals("RequestValue", read.props().get("RequestHeader"));
        HandshakeResponse written = shaker.getWrittenHeaders();
        assertEquals(1, written.props().size());
        assertEquals("OutValue", written.props().get("OutHeader"));
        ByteBuffer out = writer.getBuffer();
        assertEquals("GNUTELLA/0.6 200 OK!\r\nOutHeader: OutValue\r\n\r\n", new String(out.array(), 0, out.limit()));
    }
