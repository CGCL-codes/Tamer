    public void testSetChannel() {
        assertNotSame(m_channel, m_osRss.getChannel());
        m_osRss.setChannel(m_channel);
        assertEquals(m_channel, m_osRss.getChannel());
    }
