    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        String whom = "";
        String args[] = arguments.split(" ");
        SrvChannel_channel.ChanAccess newacc;
        boolean silent = false;
        if (replyto.startsWith("#")) whatchan = replyto;
        whom = "";
        newacc = SrvChannel_channel.ChanAccess.C_NONE;
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
                    whom = args[0];
                    try {
                        newacc = SrvChannel_channel.ChanAccess.valueOf("C_" + args[1].toUpperCase());
                    } catch (IllegalArgumentException iae) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid level. Valid levels are: NONE, PEON, CHANOP, MASTER, COOWNER, OWNER");
                        return;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].startsWith("#")) {
                    whom = args[1];
                    whatchan = args[0];
                    try {
                        newacc = SrvChannel_channel.ChanAccess.valueOf("C_" + args[2].toUpperCase());
                    } catch (IllegalArgumentException iae) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid level. Valid levels are: NONE, PEON, CHANOP, MASTER, COOWNER, OWNER");
                        return;
                    }
                } else if (args[1].startsWith("#")) {
                    whom = args[0];
                    whatchan = args[1];
                    try {
                        newacc = SrvChannel_channel.ChanAccess.valueOf("C_" + args[2].toUpperCase());
                    } catch (IllegalArgumentException iae) {
                        Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid level. Valid levels are: NONE, PEON, CHANOP, MASTER, COOWNER, OWNER");
                        return;
                    }
                } else {
                    Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid arguments.");
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
                            if (!Generic.Users.get(user).authhandle.equals(Generic.Users.get(whom).authhandle)) {
                                String aname = Generic.Users.get(user).authhandle;
                                String bname = Generic.Users.get(whom).authhandle;
                                if (((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(aname)) {
                                    if (!((SrvChannel) who).getChannels().get(whatchan).getUsers().containsKey(bname)) {
                                        SrvChannel_channel.ChanAccess alevel = ((SrvChannel) who).getChannels().get(whatchan).getUsers().get(aname);
                                        if (newacc.ordinal() < alevel.ordinal()) {
                                            ((SrvChannel) who).getChannels().get(whatchan).getUsers().put(Generic.Users.get(whom).authhandle, newacc);
                                            ((SrvAuth) Configuration.getSvc().get(Configuration.authservice)).getUsers().get(Generic.Users.get(whom).authhandle).WhereAccess.put(whatchan, newacc.toString());
                                            Generic.curProtocol.outPRVMSG(who, replyto, "User Added!");
                                        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You cannot grant a user higher access than yourself!");
                                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You have no access to channel!");
                                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User already exists (use chuser)!");
                            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You cannot add yourself!");
                        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: You are not authed!");
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: User is not authed!");
                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: No such user!");
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
