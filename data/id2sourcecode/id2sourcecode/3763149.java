    @Test
    public void testGetCommands256() {
        init(256);
        assertEquals(2, commands.size());
        assertEquals(0, commands.get(0).getStartDimmerId());
        assertEquals(255, commands.get(0).getChannelIds().length);
        assertEquals(255, commands.get(1).getStartDimmerId());
        assertEquals(1, commands.get(1).getChannelIds().length);
        assertEquals(0, changed);
        loader.process(0, buffer(255));
        assertEquals(0, changed);
        loader.process(255, buffer(1));
        assertEquals(1, changed);
        assertEquals(254, getChannel(0));
        assertEquals(0, getChannel(254));
        assertEquals(0, getChannel(255));
    }
