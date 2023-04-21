    public void test_getServerSocketChannelAddress() throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        assertTrue(AddressUtil.getChannelAddress(ssc) > 0);
    }
