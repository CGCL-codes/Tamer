    public String runMacro(ExternalHTTPRequests httpReq, String parm) {
        Hashtable parms = parseParms(parm);
        String last = httpReq.getRequestParameter("CHANNELBACKLOG");
        if (parms.containsKey("RESET")) {
            if (last != null) httpReq.removeRequestParameter("CHANNELBACKLOG");
            return "";
        }
        String channel = httpReq.getRequestParameter("CHANNEL");
        if (channel == null) return " @break@";
        int channelInt = CMLib.channels().getChannelIndex(channel);
        if (channelInt < 0) return " @break@";
        MOB mob = Authenticate.getAuthenticatedMob(httpReq);
        if (mob != null) {
            if (CMLib.channels().mayReadThisChannel(mob, channelInt, true)) {
                Vector que = CMLib.channels().getChannelQue(channelInt);
                while (true) {
                    int num = CMath.s_int(last);
                    last = "" + (num + 1);
                    httpReq.addRequestParameters("CHANNELBACKLOG", last);
                    if ((num < 0) || (num >= que.size())) {
                        httpReq.addRequestParameters("CHANNELBACKLOG", "");
                        if (parms.containsKey("EMPTYOK")) return "<!--EMPTY-->";
                        return " @break@";
                    }
                    boolean areareq = CMLib.channels().getChannelFlags(channelInt).contains(ChannelsLibrary.ChannelFlag.SAMEAREA);
                    CMMsg msg = (CMMsg) que.elementAt(num);
                    String str = null;
                    if ((mob == msg.source()) && (msg.sourceMessage() != null)) str = msg.sourceMessage(); else if ((mob == msg.target()) && (msg.targetMessage() != null)) str = msg.targetMessage(); else if (msg.othersMessage() != null) str = msg.othersMessage(); else str = "";
                    str = CMStrings.removeColors(str);
                    if (CMLib.channels().mayReadThisChannel(msg.source(), areareq, mob, channelInt, true)) return clearWebMacros(CMLib.coffeeFilter().fullOutFilter(mob.session(), mob, msg.source(), msg.target(), msg.tool(), CMStrings.removeColors(str), false));
                }
            }
            return "";
        }
        return "";
    }
