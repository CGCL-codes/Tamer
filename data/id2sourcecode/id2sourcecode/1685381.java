    public void testGetChannel() {
        ChatChannel channel = ChatChannel.getChannel(MAIN_CHANNEL_NAME);
        assertEquals("Main channel should be retrievable via getChannel().", mainChannel, channel);
    }
