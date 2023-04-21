    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        String channel = CMParms.combine(commands, 1);
        if ((channel == null) || (channel.length() == 0)) {
            mob.tell("You must specify a channel name. Try CHANNELS for a list.");
            return false;
        }
        int x = channel.indexOf("@");
        String mud = null;
        if (x > 0) {
            mud = channel.substring(x + 1);
            int channelInt = CMLib.channels().getChannelIndex(channel.substring(0, x).toUpperCase());
            channel = CMLib.channels().getChannelName(channelInt).toUpperCase();
            if ((channel.length() == 0) || (channelInt < 0)) {
                mob.tell("You must specify a valid channel name. Try CHANNELS for a list.");
                return false;
            }
            CMLib.intermud().i3chanwho(mob, channel, mud);
            return false;
        }
        int channelInt = CMLib.channels().getChannelIndex(channel.toUpperCase());
        channel = CMLib.channels().getChannelName(channelInt);
        if (channelInt < 0) {
            mob.tell("You must specify a valid channel name. Try CHANNELS for a list.");
            return false;
        }
        String head = "^x\n\rListening on " + channel + ":^?^.^N\n\r";
        StringBuffer buf = new StringBuffer("");
        boolean areareq = CMLib.channels().getChannelFlags(channelInt).contains(ChannelsLibrary.ChannelFlag.SAMEAREA);
        for (int s = 0; s < CMLib.sessions().size(); s++) {
            Session ses = CMLib.sessions().elementAt(s);
            MOB mob2 = ses.mob();
            if ((mob2 != null) && (mob2.soulMate() != null)) mob2 = mob2.soulMate();
            if ((CMLib.channels().mayReadThisChannel(mob, areareq, ses, channelInt)) && (mob2 != null) && (CMLib.flags().isInTheGame(mob2, true)) && ((((mob2.envStats().disposition() & EnvStats.IS_CLOAKED) == 0) || ((CMSecurity.isAllowedAnywhere(mob, "CLOAK") || CMSecurity.isAllowedAnywhere(mob, "WIZINV")) && (mob.envStats().level() >= mob2.envStats().level()))))) buf.append("^x[^?^.^N" + CMStrings.padRight(mob2.name(), 20) + "^x]^?^.^N\n\r");
        }
        if (buf.length() == 0) mob.tell(head + "Nobody!"); else mob.tell(head + buf.toString());
        return false;
    }
