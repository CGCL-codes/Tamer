    @Test
    public void testGetCommands512() {
        init(512);
        assertEquals(3, commands.size());
        assertEquals(0, commands.get(0).getStartDimmerId());
        assertEquals(255, commands.get(0).getChannelIds().length);
        assertEquals(255, commands.get(1).getStartDimmerId());
        assertEquals(255, commands.get(1).getChannelIds().length);
        assertEquals(510, commands.get(2).getStartDimmerId());
        assertEquals(2, commands.get(2).getChannelIds().length);
        assertEquals(0, changed);
        loader.process(0, buffer(255));
        assertEquals(0, changed);
        loader.process(255, buffer(255));
        assertEquals(0, changed);
        loader.process(510, buffer(2));
        assertEquals(1, changed);
        assertEquals(254, getChannel(0));
        assertEquals(0, getChannel(254));
        assertEquals(254, getChannel(255));
        assertEquals(0, getChannel(509));
        assertEquals(1, getChannel(510));
        assertEquals(0, getChannel(511));
    }
