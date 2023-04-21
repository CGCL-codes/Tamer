    @Test
    public void testUnrealPrivateMessage() {
        MessageEvent me = events.get(24110);
        assertTrue(me.getType() == PRIVATE_MESSAGE);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getNick().equals("mohadib"));
        assertTrue(me.getMessage().equals("HEYE THAR!"));
        assertTrue(me.getUserName().equals("~mohadib"));
        assertTrue(me.getHostName().equals("nix-C604915.albq.qwest.net"));
    }
