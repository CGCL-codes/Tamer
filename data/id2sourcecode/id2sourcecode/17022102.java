    public void testSimpleReadTimeout() throws Exception {
        StubReadObserver o1 = new StubReadObserver();
        SelectableChannel c1 = o1.getChannel();
        o1.setReadTimeout(1023);
        NIODispatcher.instance().registerRead(c1, o1);
        o1.waitForIOException(2000);
        assertInstanceof(SocketTimeoutException.class, o1.getIox());
        assertEquals("operation timed out (1023)", o1.getIox().getMessage());
        assertTrue(o1.isShutdown());
        assertEquals(0, o1.getReadsHandled());
        c1.close();
    }
