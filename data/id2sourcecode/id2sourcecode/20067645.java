    @Test
    public void testGetChannelManagerAfterInit() {
        initStableAppContext();
        InternalContext.setManagerLocator(managerLocator);
        ChannelManager c = AppContext.getChannelManager();
        Assert.assertSame(c, channelManager);
    }
