    @Test
    public void testHyperionChannelModes() {
        ModeEvent me = events.get(72);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#ubuntu"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("nickrud"));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 1);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'b');
        assertTrue(ma.getArgument().equals("*!*@tejava.dreamhost.com"));
        me = events.get(149);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#ubuntu"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals("FloodBot3"));
        adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 2);
        ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'z');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'b');
        assertTrue(ma.getArgument().equals("%ani1!*@*"));
    }
