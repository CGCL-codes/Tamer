    public void testBelowPointSixFails() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("GNUTELLA CONNECT/0.5\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        StubHandshakeObserver observer = new StubHandshakeObserver();
        Handshaker shaker = new AsyncIncomingHandshaker(new StubHandshakeResponder(), socket, observer);
        shaker.shake();
        socket.exchange();
        assertFalse(observer.isNoGOK());
        assertTrue(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
    }
