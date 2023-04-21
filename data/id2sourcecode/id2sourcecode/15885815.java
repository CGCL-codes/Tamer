    @Test
    public void testInviteEvent() {
        InviteEvent ie = IRCEventFactory.invite(":r0bby!n=wakawaka@guifications/user/r0bby INVITE scripy1 :#jerklib2", connection);
        assertNotNull(ie);
        assertEquals("r0bby", ie.getNick());
        assertEquals("n=wakawaka", ie.getUserName());
        assertEquals("guifications/user/r0bby", ie.getHostName());
        assertEquals("#jerklib2", ie.getChannelName());
    }
