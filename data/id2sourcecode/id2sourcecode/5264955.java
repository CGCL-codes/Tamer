    @Test
    public void actionDownChannel() {
        createGroup();
        selectChannelsInGroup(1, 1);
        model.getActionInGroupDown().action();
        Channel[] channels = getGroup(0).getChannels();
        assertEquals(channels.length, 3);
        assertEquals(channels[0].getName(), "Channel 1");
        assertEquals(channels[1].getName(), "Channel 3");
        assertEquals(channels[2].getName(), "Channel 2");
    }
