    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String whom = "";
        String args[] = arguments.split(" ");
        String what = "";
        whatchan = replyto;
        whom = user;
        if (args.length > 0 && (!(args[0].equals("")))) if (args[0].startsWith("#")) {
            whatchan = args[0];
            what = arguments.substring(whatchan.length() + 1);
        } else {
            what = arguments;
        }
        whom = whom.toLowerCase();
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvChannel) who).getChannels().containsKey(whatchan)) {
                if (Generic.Users.containsKey(whom)) {
                    if (Generic.Users.get(whom).authhandle != null) {
                        String aname = Generic.Users.get(whom).authhandle;
                        if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(aname)) {
                            if (((SrvChannel) who).getChannels().get(whatchan).getUsers().get(aname).ordinal() >= SrvChannel_channel.ChanAccess.C_MASTER.ordinal()) {
                                if (!what.trim().equals("*")) {
                                    ((SrvChannel) who).getChannels().get(whatchan).setMeta("greeting", what);
                                    Generic.curProtocol.outPRVMSG(who, replyto, "Greeting Set.");
                                } else {
                                    ((SrvChannel) who).getChannels().get(whatchan).rmMeta("greeting");
                                    Generic.curProtocol.outPRVMSG(who, replyto, "Greeting Cleared.");
                                }
                            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Must be master or greater to set greeting!");
                        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User has no access to channel!");
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User is not authed!");
                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User does not exist!");
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
