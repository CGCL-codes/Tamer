    @Test
    public void testBahamutPart() {
        PartEvent pe = events.get(61);
        assertTrue(pe.getChannelName().equals("#perkosa"));
        assertTrue(pe.getNick().equals("KeiKo"));
        assertTrue(pe.getUserName().equals("DI"));
        assertTrue(pe.getHostName().equals("s3xy.biz"));
        assertTrue(pe.getPartMessage().equals("14Looking for Inviter!"));
        pe = events.get(4);
        assertTrue(pe.getChannelName(), pe.getChannelName().equals("#perkosa"));
        assertTrue(pe.getNick().equals("_^Gracia^_"));
        assertTrue(pe.getUserName().equals("~Hatiyayan"));
        assertTrue(pe.getHostName().equals("124.195.18.42"));
        assertTrue(pe.getPartMessage().equals(""));
    }
