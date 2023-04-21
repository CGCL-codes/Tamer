    @Test
    public void digestChannels() throws ClassNotFoundException {
        final ClassLoader cls = Thread.currentThread().getContextClassLoader();
        final List<ChannelNotification> channels = ChannelsDigester.getChannels("channels.xml", cls);
        assertNotNull(channels);
        assertEquals(2, channels.size());
    }
