    public void testGetChannelList() throws Exception {
        assertNotNull("Unable to instantiate ZapListings", m_zap);
        Vector channels = m_zap.getChannelList(null, "98103", "262184", this, true);
        assertNotNull("Null channel list returned", channels);
        assertTrue("No channels returned", channels.size() > 0);
        for (int i = 0; i < channels.size(); i++) {
            Map channel = (Map) channels.get(i);
            assertTrue("Desc does not exist for this channel (" + i + ")", channel.containsKey(ZapListings3.DESC));
            String desc = (String) channel.get(ZapListings3.DESC);
            assertTrue("Desc is empty for this channel (" + i + ")", desc.length() > 0);
        }
    }
