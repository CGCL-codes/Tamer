    public void testGetChannel() {
        Game g = new Game(u1, "type", "someothername", 10, true);
        ChatChannel channel = g.getChannel();
        assertNotNull("Chat channel is null", channel);
        assertFalse("creator initally member of chat channel", channel.isMember(u1));
        assertTrue("Chat channel contains users in an empty game", channel.getMembers().isEmpty());
        g.addUser(u1);
        assertTrue("master user not member of chat channel", channel.isMember(u1));
        g.addUser(u2);
        assertTrue("user not member of chat channel", channel.isMember(u2));
        assertEquals("Chat channel contains a wrong number of users", channel.getMembers().size(), 2);
    }
