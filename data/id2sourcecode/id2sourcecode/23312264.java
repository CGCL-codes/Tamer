    @Test
    public void testSnircdChannelMessage() {
        MessageEvent me = events.get(24108);
        assertTrue(me.getType() == CHANNEL_MESSAGE);
        assertTrue(me.getNick().equals("FAH|SONYS"));
        assertTrue(me.getMessage().equals("3v3 srv on! low+ msg!!"));
        assertTrue(me.getUserName().equals("fah13"));
        assertTrue(me.getHostName().equals("SONYS.users.quakenet.org"));
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#cod4.wars"));
    }
