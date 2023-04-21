    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String whom;
        String args[] = arguments.split(" ");
        if (replyto.startsWith("#")) whatchan = replyto;
        whom = "";
        if (args.length > 0 && (!(args[0].equals("")))) {
            if (args.length == 1) {
                whom = args[0];
            } else if (args.length == 2) {
                if (args[0].startsWith("#")) {
                    whom = args[1];
                    whatchan = args[0];
                } else if (args[1].startsWith("#")) {
                    whom = args[0];
                    whatchan = args[1];
                } else {
                    Generic.curProtocol.outPRVMSG(who, replyto, "Error: Must specify a channel!");
                    return;
                }
            }
        }
        whom = whom.toLowerCase();
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvChannel) who).getChannels().containsKey(whatchan)) {
                if (Generic.Users.containsKey(whom)) {
                    if (Generic.Users.get(whom).authhandle != null) {
                        if (Generic.Users.get(user).authhandle != null) {
                            String aname = Generic.Users.get(user).authhandle;
                            if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(aname)) {
                                if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(Generic.Users.get(whom).authhandle)) {
                                    SrvChannel_channel.ChanAccess alevel = ((SrvChannel) who).getChannels().get(whatchan).getUsers().get(aname);
                                    SrvChannel_channel.ChanAccess blevel = ((SrvChannel) who).getChannels().get(whatchan).getUsers().get(Generic.Users.get(whom).authhandle);
                                    if (blevel.ordinal() < alevel.ordinal()) {
                                        ((SrvChannel) who).getChannels().get(whatchan).getUsers().remove(Generic.Users.get(whom).authhandle);
                                        ((SrvAuth) Configuration.getSvc().get(Configuration.authservice)).getUsers().get(Generic.Users.get(whom).authhandle).WhereAccess.remove(whatchan);
                                        Generic.curProtocol.outPRVMSG(who, replyto, "User Removed!");
                                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You cannot remove a user of higher access than yourself!");
                                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User already has no access to channel!");
                            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You have no access to channel!");
                        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You are not authed!");
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User is not authed!");
                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User does not exist!");
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
