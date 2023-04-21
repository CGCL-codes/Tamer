    public void testIsChannelNameSet() throws MessageException, IOException, RecipientException {
        Map channelInfo = createChannelInfoMap();
        channelInfo.put(LogicaSMSChannelAdapter.SUPPORTS_POOLING, "no");
        LogicaSMSChannelAdapter channelAdapter = new LogicaSMSChannelAdapter(CHANNEL_NAME, channelInfo);
        assertEquals("channelName not set properly", channelAdapter.getChannelName(), CHANNEL_NAME);
    }
