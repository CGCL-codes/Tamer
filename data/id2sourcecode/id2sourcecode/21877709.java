    public void onChannelModeRequest(ChannelModeRequestEvent channelModeRequestEvent) {
        System.out.println("onChannelModeRequest");
        assertEquals("onChannelModeRequest(): channel", "#sharktest", channelModeRequestEvent.getChannel());
        assertEquals("onChannelModeRequest(): size", 3, channelModeRequestEvent.getModes().length);
        assertEquals("onChannelModeRequest(): topic", ChannelMode.TOPIC_SETTABLE, channelModeRequestEvent.getModes()[0].getMode());
        assertEquals("onChannelModeRequest(): private", ChannelMode.PRIVATE, channelModeRequestEvent.getModes()[1].getMode());
        assertEquals("onChannelModeRequest(): limited", ChannelMode.USER_LIMIT, channelModeRequestEvent.getModes()[2].getMode());
        assertEquals("onChannelModeRequest(): param", "10", channelModeRequestEvent.getModes()[2].getParameter());
    }
