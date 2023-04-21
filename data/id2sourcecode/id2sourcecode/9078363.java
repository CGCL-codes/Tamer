    @Test
    public void testBahamutUserModes() {
        ModeEvent me = events.get(0);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getModeType() == ModeEvent.ModeType.USER);
        assertTrue(me.setBy(), me.setBy().equals(me.getSession().getConnectedHostName()));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 1);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'i');
        assertTrue(ma.getArgument().equals(""));
    }
