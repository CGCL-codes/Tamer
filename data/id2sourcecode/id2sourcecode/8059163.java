    @Test
    public void testBahamutServerNotice() {
        NoticeEvent event = events.get(2);
        assertTrue(event.getChannel() == null);
        assertTrue(event.getNoticeMessage().equals("*** Found your hostname, cached"));
        assertTrue(event.byWho(), event.byWho().equals("swiftco.wa.us.dal.net"));
        assertTrue(event.toWho().equals(""));
        assertTrue(event.getSession().equals(session));
    }
