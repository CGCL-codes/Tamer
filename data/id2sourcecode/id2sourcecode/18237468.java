    public void mode(IRCEvent event) {
        ModeEvent me = (ModeEvent) event;
        if (me.getModeType() == ModeEvent.ModeType.CHANNEL) {
            me.getChannel().updateModes(me.getModeAdjustments());
        } else {
            me.getSession().updateUserModes(me.getModeAdjustments());
        }
    }
