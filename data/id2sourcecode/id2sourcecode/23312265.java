    @Test
    public void testSnircdPrivateMessage() {
        MessageEvent me = events.get(24112);
        assertTrue(me.getType() == PRIVATE_MESSAGE);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getNick().equals("mohadib"));
        assertTrue(me.getMessage().equals("TESTING?"));
        assertTrue(me.getUserName().equals("~mohadib"));
        assertTrue(me.getHostName().equals("71-33-61-22.albq.qwest.net"));
    }
