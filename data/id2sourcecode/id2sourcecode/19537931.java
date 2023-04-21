    public void testGetListings() throws Exception {
        GetListings gl = new GetListings();
        Vector channels = m_zap.getChannelList(null, "98103", "262184", this, true);
        assertTrue("No channels found", channels.size() > 0);
        gl.writeChannelsToFile(channels, debugOut);
        gl.grab(channels, null, "98103", "262184", 10, 20000, this, null, debugOut);
    }
