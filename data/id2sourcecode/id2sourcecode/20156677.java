    @Test
    public void testSnircdPart() {
        PartEvent pe = events.get(2);
        assertTrue(pe.getChannelName().equals("#cod4.wars"));
        assertTrue(pe.getNick().equals("AlbCMCSG"));
        assertTrue(pe.getUserName().equals("jenna"));
        assertTrue(pe.getHostName().equals("stop.t.o.shit.la"));
        assertTrue(pe.getPartMessage().equals(""));
    }
