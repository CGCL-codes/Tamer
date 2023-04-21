    @Test
    public void testSnircdUserNotice() {
        NoticeEvent event = events.get(29);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("HELLO"));
        assertTrue(event.byWho().equals("mohadib_"));
        assertTrue(event.toWho().equals("mohadib__"));
        assertTrue(event.getSession().equals(session));
    }
