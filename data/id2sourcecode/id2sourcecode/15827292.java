    public final void testGetChannels() throws Exception {
        List channels = TestUtils.getService().getChannels();
        assertEquals("channels.size", channels.size(), 4);
    }
