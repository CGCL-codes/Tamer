    @Test
    public void testUnrealUserNotice() {
        NoticeEvent event = events.get(28);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("TESTTSS"));
        assertTrue(event.byWho().equals("mohadib_"));
        assertTrue(event.toWho().equals("mohadib__"));
        assertTrue(event.getSession().equals(session));
    }
