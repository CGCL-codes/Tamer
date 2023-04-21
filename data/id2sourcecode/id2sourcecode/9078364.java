    @Test
    public void testUnrealUserModes() {
        ModeEvent me = events.get(1);
        assertTrue(me.getChannel() == null);
        assertTrue(me.getModeType() == ModeEvent.ModeType.USER);
        assertTrue(me.setBy(), me.setBy().equals(me.getSession().getConnectedHostName()));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 3);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'i');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'w');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(2);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'x');
        assertTrue(ma.getArgument().equals(""));
    }
