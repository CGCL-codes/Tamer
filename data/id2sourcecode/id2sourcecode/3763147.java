    @Test
    public void testGetCommands32() {
        init(32);
        assertEquals(1, commands.size());
        assertEquals(0, commands.get(0).getStartDimmerId());
        assertEquals(32, commands.get(0).getChannelIds().length);
        assertEquals(0, changed);
        loader.process(0, buffer(32));
        assertEquals(1, changed);
        assertEquals(31, getChannel(0));
        assertEquals(0, getChannel(31));
    }
