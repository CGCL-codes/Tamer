    public void testGetChannelPartialName() {
        config1.setName("xzyt");
        manager.createChannel(config1, false);
        manager.createChannel(config2, false);
        Channel channel = manager.getChannel("test", true);
        assertNotNull("channel not found", channel);
        assertEquals("channel name", config2.getName(), channel.getConfig().getName());
    }
