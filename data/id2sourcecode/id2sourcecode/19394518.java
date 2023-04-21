    public void testSimpleConnectTimeout() throws Exception {
        StubConnectObserver observer = new StubConnectObserver();
        SocketChannel channel = observer.getChannel();
        if (OSUtils.isLinux()) channel.connect(new InetSocketAddress("www.google.com", 9999));
        NIODispatcher.instance().registerConnect(channel, observer, 3000);
        assertNull(observer.getIoException());
        assertNull(observer.getSocket());
        assertFalse(observer.isShutdown());
        Thread.sleep(3500);
        assertFalse(channel.isConnected());
        assertNull(observer.getSocket());
        Exception iox = observer.getIoException();
        assertNotNull(iox);
        assertInstanceof(SocketTimeoutException.class, iox);
        assertEquals("operation timed out (3000)", iox.getMessage());
        assertTrue(observer.isShutdown());
        channel.close();
    }
