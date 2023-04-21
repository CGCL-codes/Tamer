    @Test
    public void testUnrealChannelModes() {
        ModeEvent me = events.get(11);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#tvtorrents"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("ChanServ"));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 1);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'h');
        assertTrue(ma.getArgument().equals("TVTorrentsBot"));
    }
