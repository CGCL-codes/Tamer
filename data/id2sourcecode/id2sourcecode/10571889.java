    public void testGetChannelIdsNull() {
        List channelIds = broker.getChannelIds();
        Assert.assertNull(channelIds);
    }
