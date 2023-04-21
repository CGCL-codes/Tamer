    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String args[] = arguments.split(" ");
        boolean silent = false;
        whatchan = replyto;
        if (args.length > 0 && (!(args[0].equals("")))) {
            if (args[0].startsWith("#")) {
                whatchan = args[0];
            }
        }
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvChannel) who).getChannels().containsKey(whatchan)) {
                Generic.curProtocol.outSETMODE(who, whatchan, "+o", who.getname().toLowerCase());
                for (Client whom : Generic.Channels.get(whatchan).clientmodes.keySet()) {
                    if (whom.authhandle != null) {
                        String aname = whom.authhandle;
                        if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(aname)) {
                            SrvChannel_channel.ChanAccess alevel = ((SrvChannel) who).getChannels().get(whatchan).getUsers().get(aname);
                            String newmode = "-ov";
                            if (alevel.ordinal() >= SrvChannel_channel.ChanAccess.C_PEON.ordinal()) {
                                newmode = "+v";
                            }
                            if (alevel.ordinal() >= SrvChannel_channel.ChanAccess.C_CHANOP.ordinal()) {
                                newmode = "+o";
                            }
                            Generic.curProtocol.outSETMODE(who, whatchan, newmode, whom.uid.toLowerCase());
                        }
                    } else {
                        if (!Configuration.getSvc().containsKey(whom.uid.toLowerCase())) if (!whom.modes.contains("o")) Generic.curProtocol.outSETMODE(who, whatchan, "-ov", whom.uid.toLowerCase());
                    }
                }
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
