    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        PlayerStats pstats = mob.playerStats();
        if (pstats == null) return false;
        String channelName = ((String) commands.elementAt(0)).toUpperCase().trim().substring(2);
        commands.removeElementAt(0);
        int channelNum = -1;
        for (int c = 0; c < CMLib.channels().getNumChannels(); c++) {
            if (CMLib.channels().getChannelName(c).equalsIgnoreCase(channelName)) {
                channelNum = c;
                channelName = CMLib.channels().getChannelName(c);
            }
        }
        if (channelNum < 0) for (int c = 0; c < CMLib.channels().getNumChannels(); c++) {
            if (CMLib.channels().getChannelName(c).toUpperCase().startsWith(channelName)) {
                channelNum = c;
                channelName = CMLib.channels().getChannelName(c);
            }
        }
        if ((channelNum < 0) || (!CMLib.masking().maskCheck(CMLib.channels().getChannelMask(channelNum), mob, true))) {
            mob.tell("This channel is not available to you.");
            return false;
        }
        if (!CMath.isSet(pstats.getChannelMask(), channelNum)) {
            pstats.setChannelMask(pstats.getChannelMask() | (1 << channelNum));
            mob.tell("The " + channelName + " channel has been turned off.  Use `" + channelName.toUpperCase() + "` to turn it back on.");
        } else mob.tell("The " + channelName + " channel is already off.");
        return false;
    }
