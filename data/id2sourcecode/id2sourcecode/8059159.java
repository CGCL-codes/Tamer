    @Test
    public void testBahamutUserNotice() {
        NoticeEvent event = events.get(27);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("TEST?"));
        assertTrue(event.byWho().equals("mohadib_"));
        assertTrue(event.toWho().equals("mohadib__"));
        assertTrue(event.getSession().equals(session));
    }
