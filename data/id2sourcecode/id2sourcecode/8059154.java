    @Test
    public void testBahamutChannelNotice() {
        NoticeEvent event = events.get(24);
        assertTrue(event.getChannel().getName(), event.getChannel().getName().equals("#testing"));
        assertTrue(event.getNoticeMessage(), event.getNoticeMessage().equals("test"));
        assertTrue(event.byWho(), event.byWho().equals("mohadib"));
        assertTrue(event.toWho(), event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
