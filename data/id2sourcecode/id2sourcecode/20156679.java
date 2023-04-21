    @Test
    public void testUnrealPart() {
        PartEvent pe = events.get(240);
        assertTrue(pe.getChannelName().equals("#tvtorrents"));
        assertTrue(pe.getNick().equals("Meph"));
        assertTrue(pe.getUserName().equals("~Meph"));
        assertTrue(pe.getHostName().equals("nix-1637EA46.zone2.bethere.co.uk"));
        assertTrue(pe.getPartMessage().equals("Leaving"));
        pe = events.get(357);
        assertTrue(pe.getChannelName().equals("#tvtorrents"));
        assertTrue(pe.getNick().equals("steeltx"));
        assertTrue(pe.getUserName().equals("d0b46199"));
        assertTrue(pe.getHostName().equals("nix-AFABC4F2.com"));
        assertTrue(pe.getPartMessage().equals(""));
    }
