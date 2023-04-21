    public void testDiscoOnBadResponseConnectLine() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("HTTP/1.1 345 GET OFF\r\n" + "ResponseHeader: ResponseValue\r\n" + "\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        Properties outRequestProps = new Properties();
        outRequestProps.put("OutRequest", "OutRequestValue");
        StubHandshakeResponder responder = new StubHandshakeResponder();
        StubHandshakeObserver observer = new StubHandshakeObserver();
        Handshaker shaker = new AsyncOutgoingHandshaker(outRequestProps, responder, socket, observer);
        shaker.shake();
        socket.exchange();
        assertFalse(observer.isNoGOK());
        assertTrue(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
        HandshakeResponse responseTo = responder.getRespondedTo();
        assertNull(responseTo);
        HandshakeResponse read = shaker.getReadHeaders();
        assertEquals(0, read.props().size());
        HandshakeResponse written = shaker.getWrittenHeaders();
        assertEquals(1, written.props().size());
        assertEquals("OutRequestValue", written.props().get("OutRequest"));
        ByteBuffer buffer = writer.getBuffer();
        assertEquals("GNUTELLA CONNECT/0.6\r\nOutRequest: OutRequestValue\r\n\r\n", new String(buffer.array(), 0, buffer.limit()));
    }
