    @Test
    public void testHyperionPart() {
        PartEvent pe = events.get(0);
        assertTrue(pe.getChannelName().equals("#ubuntu"));
        assertTrue(pe.getNick().equals("Tmcarr89"));
        assertTrue(pe.getUserName().equals("n=tmcarr"));
        assertTrue(pe.getHostName().equals("dhcp-128-194-18-51.resnet.tamu.edu"));
        assertTrue(pe.getPartMessage().equals(""));
        pe = events.get(5);
        assertTrue(pe.getChannelName().equals("#ubuntu"));
        assertTrue(pe.getNick().equals("egc"));
        assertTrue(pe.getUserName().equals("n=gcarrill"));
        assertTrue(pe.getHostName().equals("cpe-66-25-187-182.austin.res.rr.com"));
        assertTrue(pe.getPartMessage().equals("\"Ex-Chat\""));
    }
