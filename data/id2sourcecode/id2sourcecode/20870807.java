    public void testAddUserStringStringStringStringChannelUserChannelPermission() {
        assertEquals(UserChannelPermission.VOICE, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.VOICE, uw.getUser("tester").getChannels().get(channel));
        uw.addUser("tester", "waldo", "John Doe", "sourceforge.net", channel, UserChannelPermission.OPERATOR);
        assertEquals(UserChannelPermission.OPERATOR, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.OPERATOR, uw.getUser("tester").getChannels().get(channel));
        assertNotNull(uw.getUser("tester"));
        assertEquals("tester", user.getNick());
        assertEquals("tester", uw.getUser("tester").getNick());
        assertEquals("waldo", user.getName());
        assertEquals("John Doe", user.getRealname());
        assertEquals("sourceforge.net", user.getHost());
        assertEquals("sourceforge.net", uw.getUser("tester").getHost());
    }
