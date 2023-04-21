    @Test
    public void testHyperionChannelNotice() {
        NoticeEvent event = events.get(23);
        assertTrue(event.getChannel().getName(), event.getChannel().getName().equals("#jerklib"));
        assertTrue(event.getNoticeMessage(), event.getNoticeMessage().equals("testtt"));
        assertTrue(event.byWho(), event.byWho().equals("mohadib__"));
        assertTrue(event.toWho(), event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
