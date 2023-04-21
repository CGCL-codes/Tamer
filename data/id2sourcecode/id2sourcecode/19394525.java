    public void testNoTimeoutIfInterestOff() throws Exception {
        StubReadObserver o1 = new StubReadObserver();
        SocketChannel c1 = o1.getChannel();
        c1.connect(LISTEN_ADDR);
        o1.setReadTimeout(1000);
        NIODispatcher.instance().registerRead(c1, o1);
        Thread.sleep(200);
        NIODispatcher.instance().interestRead(c1, false);
        o1.waitForIOException(2000);
        assertNull(o1.getIox());
        assertFalse(o1.isShutdown());
        assertEquals(0, o1.getReadsHandled());
        c1.close();
    }
