    @Test
    public void testUnrealNumericChannelModeReply() {
        ModeEvent me = events.get(543);
        assertTrue(me.getChannel().getName(), me.getChannel().getName().equals("#tvtorrents"));
        assertTrue(me.getModeType() == ModeEvent.ModeType.CHANNEL);
        assertTrue(me.setBy().equals(""));
        List<ModeAdjustment> adjs = me.getModeAdjustments();
        assertTrue(adjs != null);
        assertTrue(adjs.size() == 6);
        ModeAdjustment ma = adjs.get(0);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'n');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(1);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 't');
        assertTrue(ma.getArgument().equals(""));
        ma = adjs.get(5);
        assertTrue(ma.getAction() == Action.PLUS);
        assertTrue(ma.getMode() == 'f');
        assertTrue(ma.getArgument().equals("[30j#R10,30k#K10,40m#M10,10n#N10]:15"));
    }
