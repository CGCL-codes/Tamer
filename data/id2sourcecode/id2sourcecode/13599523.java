    public boolean channel(MOB mob, Vector commands, boolean systemMsg) {
        PlayerStats pstats = mob.playerStats();
        String channelName = ((String) commands.elementAt(0)).toUpperCase().trim();
        commands.removeElementAt(0);
        int channelInt = CMLib.channels().getChannelIndex(channelName);
        int channelNum = CMLib.channels().getChannelCodeNumber(channelName);
        if ((pstats != null) && (CMath.isSet(pstats.getChannelMask(), channelInt))) {
            pstats.setChannelMask(pstats.getChannelMask() & (pstats.getChannelMask() - channelNum));
            mob.tell(channelName + " has been turned on.  Use `NO" + channelName.toUpperCase() + "` to turn it off again.");
            return false;
        }
        if (CMath.bset(mob.getBitmap(), MOB.ATT_QUIET)) {
            mob.tell("You have QUIET mode on.  You must turn it off first.");
            return false;
        }
        if (commands.size() == 0) {
            mob.tell(channelName + " what?");
            return false;
        }
        for (int i = 0; i < commands.size(); i++) {
            String s = (String) commands.elementAt(i);
            if (s.indexOf(" ") >= 0) commands.setElementAt("\"" + s + "\"", i);
        }
        if (!CMLib.masking().maskCheck(CMLib.channels().getChannelMask(channelInt), mob, true)) {
            mob.tell("This channel is not available to you.");
            return false;
        }
        HashSet<ChannelsLibrary.ChannelFlag> flags = CMLib.channels().getChannelFlags(channelInt);
        if ((mob.getClanID().equalsIgnoreCase("") || mob.getClanRole() == Clan.POS_APPLICANT) && (flags.contains(ChannelsLibrary.ChannelFlag.CLANONLY) || flags.contains(ChannelsLibrary.ChannelFlag.CLANALLYONLY))) {
            mob.tell("You can't talk to your clan - you don't have one.");
            return false;
        }
        if ((commands.size() == 2) && (mob.session() != null) && (((String) commands.firstElement()).equalsIgnoreCase("last")) && (CMath.isNumber((String) commands.lastElement()))) {
            int num = CMath.s_int((String) commands.lastElement());
            Vector que = CMLib.channels().getChannelQue(channelInt);
            boolean showedAny = false;
            if (que.size() > 0) {
                if (num > que.size()) num = que.size();
                boolean areareq = flags.contains(ChannelsLibrary.ChannelFlag.SAMEAREA);
                for (int i = que.size() - num; i < que.size(); i++) {
                    CMMsg msg = (CMMsg) que.elementAt(i);
                    showedAny = CMLib.channels().channelTo(mob.session(), areareq, channelInt, msg, msg.source()) || showedAny;
                }
            }
            if (!showedAny) {
                mob.tell("There are no previous entries on this channel.");
                return false;
            }
        } else if (flags.contains("READONLY")) {
            mob.tell("This channel is read-only.");
            return false;
        } else if (flags.contains("PLAYERREADONLY") && (!mob.isMonster())) {
            mob.tell("This channel is read-only.");
            return false;
        } else CMLib.channels().reallyChannel(mob, channelName, CMParms.combine(commands, 0), systemMsg);
        return false;
    }
