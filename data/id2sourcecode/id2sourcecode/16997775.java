    public void testCrawlerDiscosEarly() throws Exception {
        ReadBufferChannel reader = new ReadBufferChannel(("GNUTELLA CONNECT/0.6\r\n" + "Crawler: 0.1\r\n" + "\r\n" + "GNUTELLA/0.6 200 OK DOKIE\r\n" + "ResponseHeader: ResponseValue\r\n" + "\r\n").getBytes());
        WriteBufferChannel writer = new WriteBufferChannel(2048);
        MultiplexingSocket socket = new MultiplexingSocket(reader, writer);
        StubHandshakeObserver observer = new StubHandshakeObserver();
        HandshakeResponse response = new StubHandshakeResponse(HandshakeResponse.CRAWLER_CODE, "Failed", new Properties());
        Handshaker shaker = new AsyncIncomingHandshaker(new StubHandshakeResponder(response), socket, observer);
        shaker.shake();
        socket.exchange();
        assertFalse(observer.isNoGOK());
        assertTrue(observer.isBadHandshake());
        assertFalse(observer.isHandshakeFinished());
        assertNull(observer.getShaker());
    }
