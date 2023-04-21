    @Test
    public void testSnircdServerNotice() {
        NoticeEvent event = events.get(20);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("Highest connection count: 5736 (5735 clients)"));
        assertTrue(event.byWho(), event.byWho().equals("underworld2.no.quakenet.org"));
        assertTrue(event.toWho(), event.toWho().equals(session.getNick()));
        assertTrue(event.getSession().equals(session));
    }
