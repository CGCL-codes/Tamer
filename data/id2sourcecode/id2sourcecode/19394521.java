    public void testAcceptChannel() throws Exception {
        if (OSUtils.isLinux()) return;
        LISTEN_SOCKET.close();
        ServerSocketChannel channel = ServerSocketChannel.open();
        LISTEN_SOCKET = channel.socket();
        channel.configureBlocking(false);
        LISTEN_SOCKET.setReuseAddress(true);
        LISTEN_SOCKET.bind(new InetSocketAddress(LISTEN_PORT));
        StubAcceptChannelObserver observer = new StubAcceptChannelObserver();
        NIODispatcher.instance().registerAccept(channel, observer);
        SocketChannel c1 = new StubConnectObserver().getChannel();
        SocketChannel c2 = new StubConnectObserver().getChannel();
        SocketChannel c3 = new StubConnectObserver().getChannel();
        assertEquals(0, observer.getChannels().size());
        NIODispatcher.instance().registerConnect(c1, new StubConnectObserver(), 0);
        c1.connect(LISTEN_ADDR);
        Thread.sleep(300);
        assertEquals(1, observer.getChannels().size());
        SocketChannel r1 = observer.getNextSocketChannel();
        assertEquals(c1.socket().getLocalPort(), r1.socket().getPort());
        assertFalse(r1.isBlocking());
        assertTrue(r1.isConnected());
        c1.close();
        NIODispatcher.instance().registerConnect(c2, new StubConnectObserver(), 0);
        NIODispatcher.instance().registerConnect(c3, new StubConnectObserver(), 0);
        c2.connect(LISTEN_ADDR);
        c3.connect(LISTEN_ADDR);
        Thread.sleep(300);
        assertEquals(2, observer.getChannels().size());
        SocketChannel r2 = observer.getNextSocketChannel();
        SocketChannel r3 = observer.getNextSocketChannel();
        if (c2.socket().getLocalPort() != r2.socket().getPort()) {
            SocketChannel temp = r3;
            r3 = r2;
            r2 = temp;
        }
        assertEquals(c2.socket().getLocalPort(), r2.socket().getPort());
        assertFalse(r2.isBlocking());
        assertTrue(r2.isConnected());
        assertEquals(c3.socket().getLocalPort(), r3.socket().getPort());
        assertFalse(r3.isBlocking());
        assertTrue(r3.isConnected());
        c2.close();
        c3.close();
    }
