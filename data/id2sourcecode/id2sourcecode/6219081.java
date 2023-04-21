    public void executeMsg(Environmental myHost, CMMsg msg) {
        super.executeMsg(myHost, msg);
        if ((CMath.bset(msg.othersMajor(), CMMsg.MASK_CHANNEL))) {
            int channelInt = msg.othersMinor() - CMMsg.TYP_CHANNEL;
            boolean areareq = CMLib.channels().getChannelFlags(channelInt).contains(ChannelsLibrary.ChannelFlag.SAMEAREA);
            if ((CMLib.channels().getChannelFlags(channelInt).contains(ChannelsLibrary.ChannelFlag.CLANONLY) || CMLib.channels().getChannelFlags(channelInt).contains(ChannelsLibrary.ChannelFlag.CLANALLYONLY)) && (invoker() != null) && (invoker().getClanID().length() > 0) && (!((MOB) affected).getClanID().equals(invoker().getClanID())) && (!CMLib.channels().mayReadThisChannel(msg.source(), areareq, invoker(), channelInt))) invoker.executeMsg(myHost, msg);
        }
    }
