    @Test
    public void testHyperionServerNotice() {
        NoticeEvent event = events.get(30);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("NickServ set your hostname to \"unaffiliated/mohadib\""));
        assertTrue(event.byWho(), event.byWho().equals("kubrick.freenode.net"));
        assertTrue(event.toWho().equals("mohadib"));
        assertTrue(event.getSession().equals(session));
    }
