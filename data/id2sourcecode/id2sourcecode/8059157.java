    @Test
    public void testSnircdChannelNotice() {
        NoticeEvent event = events.get(26);
        assertTrue(event.getChannel().getName(), event.getChannel().getName().equals("#foooo"));
        assertTrue(event.getNoticeMessage(), event.getNoticeMessage().equals("heloooo"));
        assertTrue(event.byWho(), event.byWho().equals("mohadib"));
        assertTrue(event.toWho(), event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
