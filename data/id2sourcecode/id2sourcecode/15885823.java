    @Test
    public void testGenericNoticeEvent() {
        NoticeEvent ne = IRCEventFactory.notice("NOTICE AUTH :*** No identd (auth) response", connection);
        assertNotNull(ne);
        assertEquals("generic", ne.getNoticeType());
        assertNull(ne.getChannel());
        assertEquals("", ne.byWho());
        assertEquals("AUTH :*** No identd (auth) response", ne.getNoticeMessage());
        assertEquals("", ne.toWho());
    }
