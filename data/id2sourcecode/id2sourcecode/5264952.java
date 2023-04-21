    @Test
    public void actionAddToGroup() {
        createGroup();
        selectChannelsNotInGroup(4, 5);
        model.getActionAddToGroup().action();
        Channel[] channels = getGroup(0).getChannels();
        assertEquals(channels.length, 5);
        assertEquals(channels[0].getName(), "Channel 1");
        assertEquals(channels[1].getName(), "Channel 2");
        assertEquals(channels[2].getName(), "Channel 3");
        assertEquals(channels[3].getName(), "Channel 8");
        assertEquals(channels[4].getName(), "Channel 9");
    }
