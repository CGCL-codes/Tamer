    @Test
    public void testBahamutChannelMessage() {
        MessageEvent me = events.get(22);
        assertTrue(me.getType() == CHANNEL_MESSAGE);
        assertTrue(me.getNick().equals("p3rkosa"));
        assertTrue(me.getUserName(), me.getUserName().equals("perkosa"));
        assertTrue(me.getHostName().equals("72.20.54.161"));
        assertTrue(me.getChannel().getName().equals("#perkosa"));
        assertTrue(me.getMessage().equals("1230 06secs remaining"));
    }
