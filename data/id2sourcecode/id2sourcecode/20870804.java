    public void testAddUserStringStringString() {
        uw.addUser("tester", "ludwig", "sf.net");
        assertEquals(UserChannelPermission.VOICE, user.getChannels().get(channel));
        assertEquals(UserChannelPermission.VOICE, uw.getUser("tester").getChannels().get(channel));
        assertNotNull(uw.getUser("tester"));
        assertEquals("tester", user.getNick());
        assertEquals("tester", uw.getUser("tester").getNick());
        assertEquals("ludwig", user.getName());
        assertEquals("TesterRealName", user.getRealname());
        assertEquals("sf.net", user.getHost());
        assertEquals("sf.net", uw.getUser("tester").getHost());
    }
