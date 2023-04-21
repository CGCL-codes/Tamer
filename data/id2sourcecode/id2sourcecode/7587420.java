    public String getExtraChannelDesc(String channelName) {
        StringBuilder str = new StringBuilder("");
        int dex = getChannelIndex(channelName);
        if (dex >= 0) {
            HashSet<ChannelFlag> flags = getChannelFlags(dex);
            String mask = getChannelMask(dex);
            if (flags.contains(ChannelFlag.CLANALLYONLY)) str.append(" This is a channel for clans and their allies.");
            if (flags.contains(ChannelFlag.CLANONLY)) str.append(" Only members of the same clan can see messages on this channel.");
            if (flags.contains(ChannelFlag.PLAYERREADONLY) || flags.contains(ChannelFlag.READONLY)) str.append(" This channel is read-only.");
            if (flags.contains(ChannelFlag.SAMEAREA)) str.append(" Only people in the same area can see messages on this channel.");
            if ((mask != null) && (mask.trim().length() > 0)) str.append(" The following may read this channel : " + CMLib.masking().maskDesc(mask));
        }
        return str.toString();
    }
