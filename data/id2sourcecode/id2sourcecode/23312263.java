    @Test
    public void testHyperionPrivateMessage() {
        MessageEvent me = events.get(24111);
        assertTrue(me.getType() == PRIVATE_MESSAGE);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getNick().equals("mohadib"));
        assertTrue(me.getMessage().equals("HELLO"));
        assertTrue(me.getUserName().equals("n=mohadib"));
        assertTrue(me.getHostName().equals("unaffiliated/mohadib"));
    }
