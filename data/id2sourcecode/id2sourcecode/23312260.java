    @Test
    public void testUnrealChannelMessage() {
        MessageEvent me = events.get(576);
        assertTrue(me.getType() == CHANNEL_MESSAGE);
        assertTrue(me.getNick().equals("steff"));
        assertTrue(me.getUserName().equals("~steff.hah"));
        assertTrue(me.getHostName().equals("nix-F0F6441A.static.tpgi.com.au"));
        assertTrue(me.getChannel().getName().equals("#tvtorrents"));
        assertTrue(me.getMessage().equals("InCrediBot credits tori_live"));
    }
