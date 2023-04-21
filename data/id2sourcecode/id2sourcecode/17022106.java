    public void testTimeoutBecomesSmaller() throws Exception {
        StubReadConnectObserver o1 = new StubReadConnectObserver();
        SocketChannel c1 = o1.getChannel();
        NIODispatcher.instance().registerConnect(c1, o1, 1000);
        c1.connect(LISTEN_ADDR);
        o1.waitForEvent(1000);
        assertEquals(c1.socket(), o1.getSocket());
        assertTrue(c1.isConnected());
        LISTEN_SOCKET.setSoTimeout(5000);
        Socket accepted = LISTEN_SOCKET.accept();
        Thread.sleep(2000);
        assertTrue(c1.isConnected());
        assertFalse(o1.isShutdown());
        assertNull(o1.getIoException());
        assertEquals(0, o1.getReadsHandled());
        o1.setReadTimeout(5000);
        NIODispatcher.instance().interestRead(c1, true);
        o1.setReadTimeout(1000);
        accepted.getOutputStream().write(new byte[100]);
        Thread.sleep(500);
        assertGreaterThanOrEquals(1, o1.getReadsHandled());
        Thread.sleep(1500);
        assertInstanceof(SocketTimeoutException.class, o1.getIoException());
        assertEquals("operation timed out (1000)", o1.getIoException().getMessage());
        Thread.sleep(5500);
        assertEquals("operation timed out (1000)", o1.getIoException().getMessage());
        c1.close();
    }
