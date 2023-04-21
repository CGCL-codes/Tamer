    public void testChangingTimeoutByInterest() throws Exception {
        StubReadObserver o1 = new StubReadObserver();
        SelectableChannel c1 = o1.getChannel();
        o1.setReadTimeout(1000);
        NIODispatcher.instance().registerRead(c1, o1);
        Thread.sleep(200);
        o1.setReadTimeout(2005);
        NIODispatcher.instance().interestRead(c1, true);
        o1.waitForIOException(4000);
        assertInstanceof(SocketTimeoutException.class, o1.getIox());
        assertEquals("operation timed out (2005)", o1.getIox().getMessage());
        assertTrue(o1.isShutdown());
        assertEquals(0, o1.getReadsHandled());
        c1.close();
    }
