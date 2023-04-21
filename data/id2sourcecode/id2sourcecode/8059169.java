    @Test
    public void testHyperionGenericNotices() {
        NoticeEvent event = events.get(7);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("*** No identd (auth) response"));
        assertTrue(event.byWho(), event.byWho().equals("anthony.freenode.net"));
        assertTrue(event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
