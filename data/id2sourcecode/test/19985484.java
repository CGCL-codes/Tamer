    public void testDiscoOnBadResponseCode() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("GNUTELLA/0.6 544 SHUCKS\r\n" + "ResponseHeader: ResponseValue\r\n" + "\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        Properties outRequestProps = new Properties();
        outRequestProps.put("OutRequest", "OutRequestValue");
        StubHandshakeResponder responder = new StubHandshakeResponder();
        StubHandshakeObserver observer = new StubHandshakeObserver();
        Handshaker shaker = new AsyncOutgoingHandshaker(outRequestProps, responder, socket, observer);
        shaker.shake();
        socket.exchange();
        assertTrue(observer.isNoGOK());
        assertEquals(544, observer.getCode());
        assertFalse(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
        HandshakeResponse responseTo = responder.getRespondedTo();
        assertNull(responseTo);
        HandshakeResponse read = shaker.getReadHeaders();
        assertEquals(1, read.props().size());
        assertEquals("ResponseValue", read.props().get("ResponseHeader"));
        HandshakeResponse written = shaker.getWrittenHeaders();
        assertEquals(1, written.props().size());
        assertEquals("OutRequestValue", written.props().get("OutRequest"));
        ByteBuffer buffer = writer.getBuffer();
        assertEquals("GNUTELLA CONNECT/0.6\r\nOutRequest: OutRequestValue\r\n\r\n", new String(buffer.array(), 0, buffer.limit()));
    }
