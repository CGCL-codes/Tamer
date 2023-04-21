    @Test
    public void testKickEvent() {
        KickEvent ke = IRCEventFactory.kick(":mohadib!~mohadib@67.41.102.162 KICK #test scab :bye!", connection);
        assertNotNull(ke);
        assertEquals("mohadib", ke.byWho());
        assertEquals("~mohadib", ke.getUserName());
        assertEquals("67.41.102.162", ke.getHostName());
        assertEquals("#test", ke.getChannel().getName());
        assertEquals("scab", ke.getWho());
        assertEquals("bye!", ke.getMessage());
    }
