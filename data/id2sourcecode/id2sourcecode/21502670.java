    public boolean okMessage(Environmental myHost, CMMsg msg) {
        if (!super.okMessage(myHost, msg)) return false;
        if ((msg.othersMajor() & CMMsg.MASK_CHANNEL) > 0) {
            int channelInt = msg.othersMinor() - CMMsg.TYP_CHANNEL;
            if ((msg.source() == affected) || (!(affected instanceof MOB)) && ((channels == null) || (channels.size() == 0) || (channels.contains(CMLib.channels().getChannelName(channelInt))))) {
                if (!sendOK) {
                    if (msg.source() == affected) msg.source().tell("Your message drifts into oblivion."); else if ((!(affected instanceof MOB)) && (CMLib.map().roomLocation(affected) == msg.source().location())) msg.source().tell("This is a no-channel area.");
                    return false;
                }
                if (!receive) {
                    if ((msg.source() != affected) || ((!(affected instanceof MOB)) && (CMLib.map().roomLocation(affected) != msg.source().location()))) return false;
                }
            }
        }
        return true;
    }
