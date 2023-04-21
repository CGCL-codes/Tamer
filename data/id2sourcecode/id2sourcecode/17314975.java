    final void imc_recv_chat(imc_char_data d, String from, String channel, String text, int emote) {
        StringTokenizer st = new StringTokenizer(channel, ":");
        if (st.countTokens() > 1) {
            channel = st.nextToken();
            channel = st.nextToken();
        } else if (st.countTokens() > 0) channel = st.nextToken();
        String channelName = channel;
        CMMsg msg = null;
        if (from.toUpperCase().endsWith(imc_name.toUpperCase())) return;
        if (channelName.length() == 0) return;
        channelName = read_channel_name(channelName);
        if (channelName.length() == 0) return;
        int channelInt = CMLib.channels().getChannelIndex(channelName);
        if (channelInt < 0) return;
        MOB mob = CMClass.getMOB("StdMOB");
        mob.setName(from);
        mob.setLocation(CMClass.getLocale("StdRoom"));
        String str = "^Q^<CHANNEL \"" + channelName + "\"^>" + mob.name() + " " + channelName + "(S) '" + text + "'^</CHANNEL^>^N^.";
        if (emote > 0) {
            if (emote == 1) str = "^Q^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + from + " " + text + "^</CHANNEL^>^N^."; else str = "^Q^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + text + "^</CHANNEL^>^N^.";
        }
        msg = CMClass.getMsg(mob, null, null, CMMsg.NO_EFFECT, null, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), str);
        CMLib.channels().channelQueUp(channelInt, msg);
        for (int s = 0; s < CMLib.sessions().size(); s++) {
            Session ses = CMLib.sessions().elementAt(s);
            if ((CMLib.channels().mayReadThisChannel(mob, false, ses, channelInt)) && (ses.mob().location() != null) && (ses.mob().location().okMessage(ses.mob(), msg))) ses.mob().executeMsg(ses.mob(), msg);
        }
        LinkedList l = (LinkedList) chanhist.get(channel);
        if (l == null) l = new LinkedList();
        l.add(str);
        chanhist.put(channel, l);
        Room R = mob.location();
        mob.destroy();
        if (R != null) R.destroy();
    }
