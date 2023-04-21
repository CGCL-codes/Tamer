    @Test
    public void testHyperionUserNotice() {
        NoticeEvent event = events.get(18);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("This nickname is owned by someone else"));
        assertTrue(event.byWho().equals("NickServ"));
        assertTrue(event.toWho(), event.toWho().equals("scripy"));
        assertTrue(event.getSession().equals(session));
    }
