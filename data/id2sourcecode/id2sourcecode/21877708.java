    public void onChannelModeChange(ChannelModeChangeEvent channelModeChangeEvent) {
        assertEquals("onChannelModeChange(): userInfo.getNick()", "Scurvy", channelModeChangeEvent.getUserInfo().getNick());
        assertEquals("onChannelModeChange(): userInfo.User", "~Scurvy", channelModeChangeEvent.getUserInfo().getUser());
        assertEquals("onChannelModeChange(): userInfo.Host", "pcp825822pcs.nrockv01.md.comcast.net", channelModeChangeEvent.getUserInfo().getHostName());
        assertEquals("onChannelModeChange(): channel", "#sharktest", channelModeChangeEvent.getChannel());
        assertEquals("onChannelModeChange(): action", ModeAction.ADD, channelModeChangeEvent.getModes()[0].getAction());
        assertEquals("onChannelModeChange(): size", 3, channelModeChangeEvent.getModes().length);
        assertEquals("onChannelModeChange(): ban", ChannelMode.BAN, channelModeChangeEvent.getModes()[0].getMode());
        assertEquals("onChannelModeChange(): param", "*!*@*", channelModeChangeEvent.getModes()[0].getParameter());
        assertEquals("onChannelModeChange(): invite action", ModeAction.REMOVE, channelModeChangeEvent.getModes()[1].getAction());
        assertEquals("onChannelModeChange(): invite mode", ChannelMode.INVITE_ONLY, channelModeChangeEvent.getModes()[1].getMode());
        assertEquals("onChannelModeChange(): exception action", ModeAction.ADD, channelModeChangeEvent.getModes()[2].getAction());
        assertEquals("onChannelModeChange(): exception mode", ChannelMode.EXCEPTION, channelModeChangeEvent.getModes()[2].getMode());
        assertEquals("onChannelModeChange(): exception param", "*!*@*.fi", channelModeChangeEvent.getModes()[2].getParameter());
    }
