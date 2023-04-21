    @Test
    public void testGetChannelManagerWithUnstableManagerLocator() {
        initUnstableAppContext();
        InternalContext.setManagerLocator(managerLocator);
        ChannelManager c1 = AppContext.getChannelManager();
        ChannelManager c2 = AppContext.getChannelManager();
        Assert.assertSame(c1, channelManager);
        Assert.assertNull(c2);
    }
