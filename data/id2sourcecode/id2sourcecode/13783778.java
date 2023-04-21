    public void testGetChannelByNumber() {
        String name = config1.getName();
        manager.createChannel(config1, false);
        Channel channel = manager.getChannel(0);
        assertNotNull("channel not found", channel);
        assertEquals("channel name", name, channel.getConfig().getName());
        Channel channel2 = manager.getChannel(1);
        assertNull("channel found at index 1", channel2);
    }
