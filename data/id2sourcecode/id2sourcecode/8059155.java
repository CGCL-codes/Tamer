    @Test
    public void testUnrealChannelNotice() {
        NoticeEvent event = events.get(25);
        assertTrue(event.getChannel().getName(), event.getChannel().getName().equals("#test"));
        assertTrue(event.getNoticeMessage(), event.getNoticeMessage().equals("testss"));
        assertTrue(event.byWho(), event.byWho().equals("mohadib"));
        assertTrue(event.toWho(), event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
