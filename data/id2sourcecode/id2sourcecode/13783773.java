    public void testCreateChannel() {
        assertEquals("channel count before creation", 0, manager.getChannelCount());
        manager.createChannel(config1, false);
        assertEquals("channel count after creation", 1, manager.getChannelCount());
    }
