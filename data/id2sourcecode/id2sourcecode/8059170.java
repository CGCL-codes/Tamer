    @Test
    public void testSnircdGenericNotices() {
        NoticeEvent event = events.get(8);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("*** Checking Ident"));
        assertTrue(event.byWho(), event.byWho().equals("anthony.freenode.net"));
        assertTrue(event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
