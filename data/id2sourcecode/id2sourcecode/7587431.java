    public void reallyChannel(MOB mob, String channelName, String message, boolean systemMsg) {
        int channelInt = getChannelIndex(channelName);
        if (channelInt < 0) return;
        message = CMProps.applyINIFilter(message, CMProps.SYSTEM_CHANNELFILTER);
        HashSet<ChannelFlag> flags = getChannelFlags(channelInt);
        channelName = getChannelName(channelInt);
        CMMsg msg = null;
        if (systemMsg) {
            String str = "[" + channelName + "] '" + message + "'^</CHANNEL^>^?^.";
            if ((!mob.name().startsWith("^")) || (mob.name().length() > 2)) str = "<S-NAME> " + str;
            msg = CMClass.getMsg(mob, null, null, CMMsg.MASK_CHANNEL | CMMsg.MASK_ALWAYS | CMMsg.MSG_SPEAK, "^Q^<CHANNEL \"" + channelName + "\"^>" + str, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), "^Q^<CHANNEL \"" + channelName + "\"^>" + str);
        } else if (message.startsWith(",") || (message.startsWith(":") && (message.length() > 1) && (Character.isLetter(message.charAt(1)) || message.charAt(1) == ' '))) {
            String msgstr = message.substring(1);
            Vector V = CMParms.parse(msgstr);
            Social S = CMLib.socials().fetchSocial(V, true, false);
            if (S == null) S = CMLib.socials().fetchSocial(V, false, false);
            if (S != null) msg = S.makeChannelMsg(mob, channelInt, channelName, V, false); else {
                msgstr = CMProps.applyINIFilter(msgstr, CMProps.SYSTEM_EMOTEFILTER);
                if (msgstr.trim().startsWith("'") || msgstr.trim().startsWith("`")) msgstr = msgstr.trim(); else msgstr = " " + msgstr.trim();
                String srcstr = "^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + mob.name() + msgstr + "^</CHANNEL^>^N^.";
                String reststr = "^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] <S-NAME>" + msgstr + "^</CHANNEL^>^N^.";
                msg = CMClass.getMsg(mob, null, null, CMMsg.MASK_CHANNEL | CMMsg.MASK_ALWAYS | CMMsg.MSG_SPEAK, "^Q" + srcstr, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), "^Q" + reststr);
            }
        } else msg = CMClass.getMsg(mob, null, null, CMMsg.MASK_CHANNEL | CMMsg.MASK_ALWAYS | CMMsg.MSG_SPEAK, "^Q^<CHANNEL \"" + channelName + "\"^>You " + channelName + " '" + message + "'^</CHANNEL^>^N^.", CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), "^Q^<CHANNEL \"" + channelName + "\"^><S-NAME> " + channelName + "S '" + message + "'^</CHANNEL^>^N^.");
        if ((mob.location() != null) && ((!mob.location().isInhabitant(mob)) || (mob.location().okMessage(mob, msg)))) {
            boolean areareq = flags.contains(ChannelsLibrary.ChannelFlag.SAMEAREA);
            channelQueUp(channelInt, msg);
            for (int s = 0; s < CMLib.sessions().size(); s++) {
                Session ses = CMLib.sessions().elementAt(s);
                channelTo(ses, areareq, channelInt, msg, mob);
            }
        }
        if ((CMLib.intermud().i3online() && (CMLib.intermud().isI3channel(channelName))) || (CMLib.intermud().imc2online() && (CMLib.intermud().isIMC2channel(channelName)))) CMLib.intermud().i3channel(mob, channelName, message);
    }
