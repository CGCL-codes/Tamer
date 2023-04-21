    @Test
    public void testHyperionNumericChannelModeReply() {
        ModeEvent me = events.get(545);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#jerklib"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals(""));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 2);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 't');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'n');
        assertTrue(ma.getArgument().equals(""));
    }
