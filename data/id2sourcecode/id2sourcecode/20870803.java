    public void testAddUserString() {
        uw.addUser("tester");
        uw.addUser("waldo");
        assertEquals(UserChannelPermission.VOICE, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.VOICE, uw.getUser("tester").getChannels().get(channel));
        assertNotNull(uw.getUser("tester"));
        assertEquals("tester", user.getNick());
        assertEquals("tester", uw.getUser("tester").getNick());
        assertEquals("TesterName", user.getName());
        assertEquals("TesterRealName", user.getRealname());
        assertEquals("testing.com", user.getHost());
        assertEquals("testing.com", uw.getUser("tester").getHost());
        assertTrue(uw.getUser("waldo").getChannels().isEmpty());
    }
