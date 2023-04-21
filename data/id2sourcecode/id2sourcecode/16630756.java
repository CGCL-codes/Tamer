    @Test
    public void testUnrealJoinComplete() {
        JoinEvent je = (JoinEvent) events.get(18);
        assertTrue(je.getChannel().getName(), je.getChannel().getName().equals("#tvtorrents"));
        assertTrue(je.getNick().equals("TVTorrentsBot"));
        assertTrue(je.getHostName().equals("nix-555C426C.cust.blixtvik.net"));
        assertTrue(je.getUserName(), je.getUserName().equals("~PircBot"));
    }
