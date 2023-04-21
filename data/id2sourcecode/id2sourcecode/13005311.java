    public void test_getNonNativeChannelAddress() throws Exception {
        Channel channel = new MockChannel();
        assertEquals(0, AddressUtil.getChannelAddress(channel));
    }
