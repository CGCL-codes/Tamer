    public void testReRegisterDiscardsOldAttachment() throws Exception {
        StubReadObserver o1 = new StubReadObserver();
        SocketChannel c1 = o1.getChannel();
        c1.connect(LISTEN_ADDR);
        o1.setReadTimeout(1023);
        NIODispatcher.instance().registerRead(c1, o1);
        StubReadObserver o2 = new StubReadObserver();
        o2.setReadTimeout(1001);
        NIODispatcher.instance().registerRead(c1, o2);
        o2.waitForIOException(2000);
        assertInstanceof(SocketTimeoutException.class, o2.getIox());
        assertEquals("operation timed out (1001)", o2.getIox().getMessage());
        assertTrue(o2.isShutdown());
        assertEquals(0, o2.getReadsHandled());
        assertNull(o1.getIox());
        assertFalse(o1.isShutdown());
        assertEquals(0, o1.getReadsHandled());
        c1.close();
    }
