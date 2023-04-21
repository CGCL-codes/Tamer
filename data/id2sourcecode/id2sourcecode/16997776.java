    public void testDiscoOnBadResponder() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("GNUTELLA CONNECT/0.6\r\n" + "RequestHeader: RequestValue\r\n" + "\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        Properties outProps = new Properties();
        outProps.put("OutHeader", "OutValue");
        StubHandshakeResponder responder = new StubHandshakeResponder(new StubHandshakeResponse(599, "NOPE", outProps));
        StubHandshakeObserver observer = new StubHandshakeObserver();
        Handshaker shaker = new AsyncIncomingHandshaker(responder, socket, observer);
        shaker.shake();
        socket.exchange();
        assertTrue(observer.isNoGOK());
        assertEquals(599, observer.getCode());
        assertFalse(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
        assertEquals(1, shaker.getReadHeaders().props().size());
        assertEquals("RequestValue", shaker.getReadHeaders().props().get("RequestHeader"));
        assertEquals(1, shaker.getWrittenHeaders().props().size());
        assertEquals("OutValue", shaker.getWrittenHeaders().props().get("OutHeader"));
    }
