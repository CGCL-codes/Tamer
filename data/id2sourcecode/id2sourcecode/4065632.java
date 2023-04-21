    public void test_configureBlocking_Z_IllegalBlockingMode() throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        Selector acceptSelector = SelectorProvider.provider().openSelector();
        SelectionKey acceptKey = sc.register(acceptSelector, SelectionKey.OP_READ, null);
        assertEquals(sc.keyFor(acceptSelector), acceptKey);
        SelectableChannel getChannel = sc.configureBlocking(false);
        assertEquals(getChannel, sc);
        try {
            sc.configureBlocking(true);
            fail("Should throw IllegalBlockingModeException");
        } catch (IllegalBlockingModeException e) {
        }
    }
