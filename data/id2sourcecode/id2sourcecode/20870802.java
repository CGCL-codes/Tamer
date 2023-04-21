    public void testAddUserStringChannelUserChannelPermission() {
        assertEquals(UserChannelPermission.VOICE, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.VOICE, uw.getUser("tester").getChannels().get(channel));
        uw.addUser("tester", channel, UserChannelPermission.STANDARD);
        assertEquals(UserChannelPermission.STANDARD, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.STANDARD, uw.getUser("tester").getChannels().get(channel));
        uw.addUser("tester", channel, UserChannelPermission.OPERATOR);
        assertEquals(UserChannelPermission.OPERATOR, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.OPERATOR, uw.getUser("tester").getChannels().get(channel));
        assertNotNull(uw.getUser("tester"));
        assertEquals("tester", user.getNick());
        assertEquals("tester", uw.getUser("tester").getNick());
        assertEquals("TesterName", user.getName());
        assertEquals("TesterRealName", user.getRealname());
        assertEquals("testing.com", user.getHost());
        assertEquals("testing.com", uw.getUser("tester").getHost());
    }
