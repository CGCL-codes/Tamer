    @Test
    public void testJoinEvent() {
        JoinEvent je = IRCEventFactory.regularJoin(":r0bby!n=wakawaka@guifications/user/r0bby JOIN :#test", connection);
        assertNotNull(je);
        assertEquals("r0bby", je.getNick());
        assertEquals("n=wakawaka", je.getUserName());
        assertEquals("guifications/user/r0bby", je.getHostName());
        assertEquals("#test", je.getChannelName());
    }
