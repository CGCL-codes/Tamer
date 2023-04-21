    public boolean execute(MOB mob, Vector commands, int metaFlags) throws java.io.IOException {
        PlayerStats pstats = mob.playerStats();
        if (pstats == null) return false;
        StringBuffer buf = new StringBuffer("Available channels: \n\r");
        int col = 0;
        String[] names = CMLib.channels().getChannelNames();
        for (int x = 0; x < names.length; x++) if (CMLib.masking().maskCheck(CMLib.channels().getChannelMask(x), mob, true)) {
            if ((++col) > 3) {
                buf.append("\n\r");
                col = 1;
            }
            String channelName = names[x];
            boolean onoff = CMath.isSet(pstats.getChannelMask(), x);
            buf.append(CMStrings.padRight("^<CHANNELS '" + (onoff ? "" : "NO") + "'^>" + channelName + "^</CHANNELS^>" + (onoff ? " (OFF)" : ""), 24));
        }
        if (names.length == 0) buf.append("None!"); else buf.append("\n\rUse NOCHANNELNAME (ex: NOGOSSIP) to turn a channel off.");
        mob.tell(buf.toString());
        return false;
    }
