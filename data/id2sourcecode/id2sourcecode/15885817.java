    @Test
    public void testPartEvent() {
        PartEvent pe = IRCEventFactory.part(":r0bby!n=wakawaka@guifications/user/r0bby PART #test :FOO", connection);
        assertNotNull(pe);
        assertEquals("r0bby", pe.getWho());
        assertEquals("n=wakawaka", pe.getUserName());
        assertEquals("guifications/user/r0bby", pe.getHostName());
        assertEquals("#test", pe.getChannelName());
        assertEquals("FOO", pe.getPartMessage());
    }
