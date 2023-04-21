    @Test
    public void testNoticeEvent() {
        NoticeEvent ne = IRCEventFactory.notice(":DIBLET!n=fran@c-68-35-11-181.hsd1.nm.comcast.net NOTICE #test :test", connection);
        assertNotNull(ne);
        assertEquals("channel", ne.getNoticeType());
        assertEquals("#test", ne.getChannel().getName());
        assertEquals("DIBLET", ne.byWho());
        assertEquals("test", ne.getNoticeMessage());
        assertEquals("", ne.toWho());
    }
