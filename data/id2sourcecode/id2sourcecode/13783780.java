    public void testRemoveChannel() {
        manager.createChannel(config1, false);
        assertEquals("channel count before removal", 1, manager.getChannelCount());
        manager.removeChannel(config1.getName());
        assertEquals("channel count after removal", 0, manager.getChannelCount());
    }
