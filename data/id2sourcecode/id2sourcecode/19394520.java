    public void testConnectPreventsTimeout() throws Exception {
        if (OSUtils.isLinux()) return;
        StubConnectObserver observer = new StubConnectObserver();
        SocketChannel channel = observer.getChannel();
        NIODispatcher.instance().registerConnect(channel, observer, 3000);
        channel.connect(LISTEN_ADDR);
        Thread.sleep(1000);
        assertTrue(channel.isConnected());
        assertNull(observer.getIoException());
        assertNotNull(observer.getSocket());
        assertFalse(observer.isShutdown());
        Thread.sleep(2500);
        assertTrue(channel.isConnected());
        assertEquals(0, interestOps(channel));
        Exception iox = observer.getIoException();
        assertNull(iox);
        channel.close();
    }
