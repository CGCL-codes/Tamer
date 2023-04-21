    @Test
    public void testUnrealServerNotice() {
        NoticeEvent event = events.get(14);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("*** Looking up your hostname..."));
        assertTrue(event.byWho(), event.byWho().equals("irc.nixgeeks.com"));
        assertTrue(event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
