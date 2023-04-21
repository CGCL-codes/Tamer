    public void testGetChannels() throws Exception {
        QueryAgent agent = new QueryAgent();
        agent.connect(hostname);
        List<ChannelInfo> channels = agent.getChannels();
        agent.disconnect();
        assertNotNull("null list", channels);
        assertFalse("list is empty", channels.isEmpty());
    }
