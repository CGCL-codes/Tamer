    @Test
    public void testUserNoticeEvent() {
        NoticeEvent ne = IRCEventFactory.notice(":NickServ!NickServ@services. NOTICE mohadib_ :This nickname is owned by someone else", connection);
        assertNotNull(ne);
        assertEquals("user", ne.getNoticeType());
        assertNull(ne.getChannel());
        assertEquals("NickServ", ne.byWho());
        assertEquals("This nickname is owned by someone else", ne.getNoticeMessage());
        assertEquals("mohadib_", ne.toWho());
    }
