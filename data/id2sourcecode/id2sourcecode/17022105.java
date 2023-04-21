    public void testTimeoutUpsAfterReads() throws Exception {
        StubReadConnectObserver o1 = new StubReadConnectObserver();
        SocketChannel c1 = o1.getChannel();
        o1.setReadTimeout(1000);
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
        NIODispatcher.instance().interestRead(c1, true);
        o1.setReadTimeout(2000);
        accepted.getOutputStream().write(new byte[100]);
        Thread.sleep(500);
        assertGreaterThanOrEquals(1, o1.getReadsHandled());
        Thread.sleep(2500);
        assertInstanceof(SocketTimeoutException.class, o1.getIoException());
        assertEquals("operation timed out (2000)", o1.getIoException().getMessage());
        assertGreaterThanOrEquals(o1.getLastReadTime() + 1000, o1.getIoxTime());
        c1.close();
    }
