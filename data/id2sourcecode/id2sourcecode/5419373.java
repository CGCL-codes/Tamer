    public void receive(Packet packet) {
        switch(packet.type) {
            case Packet.CHAN_EMOTE:
            case Packet.CHAN_MESSAGE:
            case Packet.CHAN_TARGET:
                {
                    ChannelPacket ck = (ChannelPacket) packet;
                    String channelName = ck.channel;
                    CMMsg msg = null;
                    if ((ck.sender_mud != null) && (ck.sender_mud.equalsIgnoreCase(getMudName()))) return;
                    if ((ck.channel == null) || (ck.channel.length() == 0)) return;
                    int channelInt = CMLib.channels().getChannelIndex(channelName);
                    if (channelInt < 0) return;
                    ck.message = fixColors(CMProps.applyINIFilter(ck.message, CMProps.SYSTEM_CHANNELFILTER));
                    if (ck.message_target != null) ck.message_target = fixColors(CMProps.applyINIFilter(ck.message_target, CMProps.SYSTEM_CHANNELFILTER));
                    MOB mob = CMClass.getMOB("StdMOB");
                    mob.setName(ck.sender_name + "@" + ck.sender_mud);
                    mob.setLocation(CMClass.getLocale("StdRoom"));
                    MOB targetMOB = null;
                    boolean killtargetmob = false;
                    if (ck.type == Packet.CHAN_TARGET) {
                        if ((ck.target_mud != null) && (ck.target_mud.equalsIgnoreCase(getMudName()))) targetMOB = CMLib.players().getLoadPlayer(ck.target_name);
                        if ((ck.target_visible_name != null) && (ck.target_mud != null) && (targetMOB == null)) {
                            killtargetmob = true;
                            targetMOB = CMClass.getMOB("StdMOB");
                            targetMOB.setName(ck.target_visible_name + "@" + ck.target_mud);
                            targetMOB.setLocation(CMClass.getLocale("StdRoom"));
                        }
                        String msgs = socialFixIn(ck.message);
                        msgs = CMProps.applyINIFilter(msgs, CMProps.SYSTEM_EMOTEFILTER);
                        String targmsgs = socialFixIn(ck.message_target);
                        targmsgs = CMProps.applyINIFilter(targmsgs, CMProps.SYSTEM_EMOTEFILTER);
                        String str = "^Q^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + msgs + "^</CHANNEL^>^N^.";
                        String str2 = "^Q^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + targmsgs + "^</CHANNEL^>^N^.";
                        msg = CMClass.getMsg(mob, targetMOB, null, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), str2, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), str);
                    } else if (ck.type == Packet.CHAN_EMOTE) {
                        String msgs = socialFixIn(ck.message);
                        msgs = CMProps.applyINIFilter(msgs, CMProps.SYSTEM_EMOTEFILTER);
                        String str = "^Q^<CHANNEL \"" + channelName + "\"^>[" + channelName + "] " + msgs + "^</CHANNEL^>^N^.";
                        msg = CMClass.getMsg(mob, null, null, CMMsg.NO_EFFECT, null, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), str);
                    } else {
                        String str = "^Q^<CHANNEL \"" + channelName + "\"^>" + mob.name() + " " + channelName + "(S) '" + ck.message + "'^</CHANNEL^>^N^.";
                        msg = CMClass.getMsg(mob, null, null, CMMsg.NO_EFFECT, null, CMMsg.NO_EFFECT, null, CMMsg.MASK_CHANNEL | (CMMsg.TYP_CHANNEL + channelInt), str);
                    }
                    CMLib.channels().channelQueUp(channelInt, msg);
                    for (int s = 0; s < CMLib.sessions().size(); s++) {
                        Session ses = CMLib.sessions().elementAt(s);
                        if ((CMLib.channels().mayReadThisChannel(mob, false, ses, channelInt)) && (ses.mob().location() != null) && (ses.mob().location().okMessage(ses.mob(), msg))) ses.mob().executeMsg(ses.mob(), msg);
                    }
                    destroymob(mob);
                    if ((targetMOB != null) && (killtargetmob)) destroymob(targetMOB);
                }
                break;
            case Packet.LOCATE_QUERY:
                {
                    LocateQueryPacket lk = (LocateQueryPacket) packet;
                    String stat = "online";
                    String name = CMStrings.capitalizeAndLower(lk.user_name);
                    MOB smob = findSessMob(lk.user_name);
                    if (smob != null) {
                        if (CMLib.flags().isCloaked(smob)) stat = "exists, but not logged in";
                    } else if (CMLib.players().getPlayer(lk.user_name) != null) stat = "exists, but not logged in"; else if (CMLib.players().playerExists(lk.user_name)) stat = "exists, but is not online"; else name = null;
                    if (name != null) {
                        LocateReplyPacket lpk = new LocateReplyPacket(lk.sender_name, lk.sender_mud, name, 0, stat);
                        try {
                            lpk.send();
                        } catch (Exception e) {
                            Log.errOut("IMudClient", e);
                        }
                    }
                }
                break;
            case Packet.LOCATE_REPLY:
                {
                    LocateReplyPacket lk = (LocateReplyPacket) packet;
                    MOB smob = findSessMob(lk.target_name);
                    if (smob != null) smob.tell(fixColors(lk.located_visible_name) + "@" + fixColors(lk.located_mud_name) + " (" + lk.idle_time + "): " + fixColors(lk.status));
                }
                break;
            case Packet.WHO_REPLY:
                {
                    WhoPacket wk = (WhoPacket) packet;
                    MOB smob = findSessMob(wk.target_name);
                    if (smob != null) {
                        StringBuffer buf = new StringBuffer("\n\rwhois@" + fixColors(wk.sender_mud) + ":\n\r");
                        Vector V = wk.who;
                        if (V.size() == 0) buf.append("Nobody!"); else for (int v = 0; v < V.size(); v++) {
                            Vector V2 = (Vector) V.elementAt(v);
                            String nom = fixColors((String) V2.elementAt(0));
                            int idle = 0;
                            if (V2.elementAt(1) instanceof Integer) idle = ((Integer) V2.elementAt(1)).intValue();
                            String xtra = fixColors((String) V2.elementAt(2));
                            buf.append("[" + CMStrings.padRight(nom, 20) + "] " + xtra + " (" + idle + ")\n\r");
                        }
                        smob.session().wraplessPrintln(buf.toString());
                        break;
                    }
                }
                break;
            case Packet.CHAN_WHO_REP:
                {
                    ChannelWhoReply wk = (ChannelWhoReply) packet;
                    MOB smob = findSessMob(wk.target_name);
                    if (smob != null) {
                        StringBuffer buf = new StringBuffer("\n\rListening on " + wk.channel + "@" + fixColors(wk.sender_mud) + ":\n\r");
                        Vector V = wk.who;
                        if (V.size() == 0) buf.append("Nobody!"); else for (int v = 0; v < V.size(); v++) {
                            String nom = fixColors((String) V.elementAt(v));
                            buf.append("[" + CMStrings.padRight(nom, 20) + "]\n\r");
                        }
                        smob.session().wraplessPrintln(buf.toString());
                        smob.session().setPromptFlag(true);
                        break;
                    }
                }
                break;
            case Packet.CHAN_WHO_REQ:
                {
                    ChannelWhoRequest wk = (ChannelWhoRequest) packet;
                    ChannelWhoReply wkr = new ChannelWhoReply();
                    wkr.target_name = wk.sender_name;
                    wkr.target_mud = wk.sender_mud;
                    wkr.channel = wk.channel;
                    int channelInt = CMLib.channels().getChannelIndex(wk.channel);
                    Vector whoV = new Vector();
                    for (int s = 0; s < CMLib.sessions().size(); s++) {
                        Session ses = CMLib.sessions().elementAt(s);
                        if ((CMLib.channels().mayReadThisChannel(ses.mob(), false, ses, channelInt)) && ((ses.mob() == null) || (!CMLib.flags().isCloaked(ses.mob())))) whoV.addElement(ses.mob().name());
                    }
                    wkr.who = whoV;
                    try {
                        wkr.send();
                    } catch (Exception e) {
                        Log.errOut("IMudClient", e);
                    }
                }
                break;
            case Packet.CHAN_USER_REQ:
                {
                    ChannelUserRequest wk = (ChannelUserRequest) packet;
                    ChannelUserReply wkr = new ChannelUserReply();
                    wkr.target_name = wk.sender_name;
                    wkr.target_mud = wk.sender_mud;
                    wkr.userRequested = wk.userToRequest;
                    MOB M = CMLib.players().getLoadPlayer(wk.userToRequest);
                    if (M != null) {
                        wkr.userVisibleName = M.name();
                        wkr.gender = (char) M.charStats().getStat(CharStats.STAT_GENDER);
                        try {
                            wkr.send();
                        } catch (Exception e) {
                            Log.errOut("IMudClient", e);
                        }
                    }
                }
                break;
            case Packet.WHO_REQUEST:
                {
                    WhoPacket wk = (WhoPacket) packet;
                    WhoPacket wkr = new WhoPacket();
                    wkr.type = Packet.WHO_REPLY;
                    wkr.target_name = wk.sender_name;
                    wkr.target_mud = wk.sender_mud;
                    Vector whoV = new Vector();
                    for (int s = 0; s < CMLib.sessions().size(); s++) {
                        Session ses = CMLib.sessions().elementAt(s);
                        MOB smob = ses.mob();
                        if ((smob != null) && (smob.soulMate() != null)) smob = smob.soulMate();
                        if ((!ses.killFlag()) && (smob != null) && (!smob.amDead()) && (CMLib.flags().isInTheGame(smob, true)) && (!CMLib.flags().isCloaked(smob))) {
                            Vector whoV2 = new Vector();
                            whoV2.addElement(smob.name());
                            whoV2.addElement(Integer.valueOf((int) (ses.getIdleMillis() / 1000)));
                            whoV2.addElement(smob.charStats().displayClassLevel(smob, true));
                            whoV.addElement(whoV2);
                        }
                    }
                    wkr.who = whoV;
                    try {
                        wkr.send();
                    } catch (Exception e) {
                        Log.errOut("IMudClient", e);
                    }
                }
                break;
            case Packet.TELL:
                {
                    TellPacket tk = (TellPacket) packet;
                    MOB mob = CMClass.getMOB("StdMOB");
                    mob.setName(tk.sender_name + "@" + tk.sender_mud);
                    mob.setLocation(CMClass.getLocale("StdRoom"));
                    MOB smob = findSessMob(tk.target_name);
                    if (smob != null) {
                        tk.message = fixColors(CMProps.applyINIFilter(tk.message, CMProps.SYSTEM_SAYFILTER));
                        CMLib.commands().postSay(mob, smob, tk.message, true, true);
                    }
                    destroymob(mob);
                }
                break;
            default:
                Log.errOut("IMudInterface", "Unknown type: " + packet.type);
                break;
        }
    }
