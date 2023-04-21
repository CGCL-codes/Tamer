    @Test
    public void testBahamutChannelModes() {
        ModeEvent me = events.get(20);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#perkosa"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("FoNix"));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 2);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.MINUS);
        assertTrue(ma.getMode() == 'k');
        assertTrue(ma.getArgument().equals("9identified.avoice"));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'v');
        assertTrue(ma.getArgument().equals("indrii"));
        me = events.get(30);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#perkosa"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("ChanServ"));
        adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 1);
        ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'o');
        assertTrue(ma.getArgument().equals("rONx"));
    }
