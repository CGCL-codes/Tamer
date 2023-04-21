    public void onChannelList(ChannelListEvent channelListEvent) {
        System.out.println("onChannelList");
        assertEquals("onChannelList(): channel", "#sharktest", channelListEvent.getChannel());
        assertEquals("onChannelList(): mode", ChannelMode.BAN, channelListEvent.getChannelMode());
        assertEquals("onChannelList(): item", "jocko!*@*", channelListEvent.getItem());
        assertEquals("onChannelList(): who", "Scurvy", channelListEvent.getUserInfo().getNick());
        assertTrue("onChanneList(): when", 120192129 == channelListEvent.getWhenSet());
        assertTrue("onChannelList(): last", !channelListEvent.isLast());
    }
