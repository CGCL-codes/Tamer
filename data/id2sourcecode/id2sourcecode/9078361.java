    @Test
    public void testSnircdChannelModes() {
        ModeEvent me = events.get(4);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#cod4.wars"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("|cod4-wars|"));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 2);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.MINUS);
        assertTrue(ma.getMode() == 'm');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'c');
        assertTrue(ma.getArgument().equals(""));
    }
