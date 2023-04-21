    public void test_getSocketChannelAddress() throws Exception {
        SocketChannel sc = SocketChannel.open();
        assertTrue(AddressUtil.getChannelAddress(sc) > 0);
    }
