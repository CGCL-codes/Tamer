    @Test
    public void actionUpChannel() {
        createGroup();
        selectChannelsInGroup(1, 1);
        model.getActionInGroupUp().action();
        Channel[] channels = getGroup(0).getChannels();
        assertEquals(channels.length, 3);
        assertEquals(channels[0].getName(), "Channel 2");
        assertEquals(channels[1].getName(), "Channel 1");
        assertEquals(channels[2].getName(), "Channel 3");
    }
