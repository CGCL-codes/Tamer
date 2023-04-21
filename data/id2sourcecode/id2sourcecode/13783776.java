    public void testGetChannelMixedCase() {
        String name = config1.getName();
        manager.createChannel(config1, false);
        Channel channel = manager.getChannel("tEsT1");
        assertNotNull("channel not found", channel);
        assertEquals("channel name", name, channel.getConfig().getName());
    }
