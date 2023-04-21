    public void test_getDatagramChannelAddress() throws Exception {
        DatagramChannel dc = DatagramChannel.open();
        assertTrue(AddressUtil.getChannelAddress(dc) > 0);
    }
