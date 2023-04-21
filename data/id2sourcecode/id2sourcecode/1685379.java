    public void testRemoveChannel() {
        boolean removed = ChatChannel.removeChannel(MAIN_CHANNEL_NAME);
        assertTrue("Return value should indicate that main channel was removed.", removed);
        assertNull("Main channel should really have been removed.", ChatChannel.getChannel(MAIN_CHANNEL_NAME));
        assertNotNull("A new channel with the removed one's ID should be creatible.", ChatChannel.createChannel(MAIN_CHANNEL_NAME, null));
    }
