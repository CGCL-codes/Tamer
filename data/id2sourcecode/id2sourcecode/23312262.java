    @Test
    public void testHyperionChannelMessage() {
        MessageEvent me = events.get(15);
        assertTrue(me.getType() == CHANNEL_MESSAGE);
        assertTrue(me.getNick().equals("DrIP"));
        assertTrue(me.getUserName().equals("n=amrit"));
        assertTrue(me.getHostName().equals("ip68-6-164-241.sd.sd.cox.net"));
        assertTrue(me.getChannel().getName().equals("#ubuntu"));
        assertTrue(me.getMessage().equals("amenado: the wireless is wlan0 and 192.168.1.55"));
    }
