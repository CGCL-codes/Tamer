    @Test
    public void testBahamutPrivateMessage() {
        MessageEvent me = events.get(24109);
        assertTrue(me.getType() == PRIVATE_MESSAGE);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getNick().equals("mohadib"));
        assertTrue(me.getMessage().equals("HEYE THAR"));
        assertTrue(me.getUserName().equals("~mohadib"));
        assertTrue(me.getHostName().equals("71-33-61-22.albq.qwest.net"));
    }
