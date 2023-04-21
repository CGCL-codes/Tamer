    @Test
    public void testGetCommands255() {
        init(255);
        assertEquals(1, commands.size());
        assertEquals(0, commands.get(0).getStartDimmerId());
        assertEquals(255, commands.get(0).getChannelIds().length);
        assertEquals(0, changed);
        loader.process(0, buffer(255));
        assertEquals(1, changed);
        assertEquals(254, getChannel(0));
        assertEquals(0, getChannel(254));
    }
