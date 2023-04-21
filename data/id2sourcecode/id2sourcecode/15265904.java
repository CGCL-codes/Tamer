    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String whom = "";
        String args[] = arguments.split(" ");
        boolean silent = false;
        whatchan = replyto;
        whom = user;
        if (args.length > 0 && (!(args[0].equals("")))) {
            if (args[0].startsWith("#")) {
                whatchan = args[0];
            }
            if (args[0].equals("silent")) silent = true;
        }
        whom = whom.toLowerCase();
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvChannel) who).getChannels().containsKey(whatchan)) {
                if (((SrvChannel) who).getChannels().get(whatchan).getAllMeta().containsKey("_isbad")) {
                    if (Generic.Users.get(whom).authhandle != null) Generic.curProtocol.outKICK(who, whom, whatchan, "This channel has been closed by the network administration."); else Generic.curProtocol.outKILL(who, whom, "You had tried to join a channel that was closed by the network administration.");
                    Generic.modeChan(whatchan, "+si");
                }
                if (((SrvChannel) who).getChannels().get(whatchan).getAllMeta().containsKey("greeting")) if (Generic.curProtocol.getState().equals(Protocol.States.S_ONLINE)) Generic.curProtocol.outNOTICE(who, whom, "" + whatchan + ": " + ((SrvChannel) who).getChannels().get(whatchan).getMeta("greeting"));
                if (Generic.Users.get(whom).authhandle != null) {
                    String aname = Generic.Users.get(whom).authhandle;
                    if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(aname)) {
                        SrvChannel_channel.ChanAccess alevel = ((SrvChannel) who).getChannels().get(whatchan).getUsers().get(aname);
                        String newmode = "+";
                        if (alevel.ordinal() >= SrvChannel_channel.ChanAccess.C_PEON.ordinal()) newmode = "+v";
                        if (alevel.ordinal() >= SrvChannel_channel.ChanAccess.C_CHANOP.ordinal()) newmode = "+o";
                        Generic.curProtocol.outSETMODE(who, whatchan, newmode, whom);
                        if (!who.getname().equalsIgnoreCase(whom)) ((SrvChannel) who).getChannels().get(whatchan).setMeta("_ts_last", util.getTS());
                        if (((SrvChannel) who).getChannels().get(whatchan).getAllMeta().containsKey("setinfo-" + aname)) Generic.curProtocol.outPRVMSG(who, whatchan, "[" + whom + "]: " + ((SrvChannel) who).getChannels().get(whatchan).getMeta("setinfo-" + aname));
                    } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: User has no access to channel!");
                } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: User is not authed!");
            } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
