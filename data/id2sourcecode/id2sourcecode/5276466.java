    public void handle_command(Service who, String user, String replyto, String arguments) {
        SrvChannel temp = ((SrvChannel) who);
        String args[] = arguments.split(" ");
        if (args.length == 2) {
            if (Generic.Users.containsKey(args[1].toLowerCase())) {
                if (Generic.Users.get(args[1].toLowerCase()).authhandle != null) {
                    String u = Generic.Users.get(args[1].toLowerCase()).authhandle;
                    String ch = args[0].toLowerCase();
                    if (!temp.getChannels().containsKey(ch)) {
                        temp.getChannels().put(ch, new SrvChannel_channel(ch, u));
                        temp.getChannels().get(ch).getUsers().put(u, SrvChannel_channel.ChanAccess.C_OWNER);
                        ((SrvAuth) Configuration.getSvc().get(Configuration.authservice)).getUsers().get(u).WhereAccess.put(ch, SrvChannel_channel.ChanAccess.C_OWNER.toString());
                        temp.getChannels().get(ch).setMeta("_registered-by", Generic.Users.get(user.toLowerCase()).authhandle);
                        temp.getChannels().get(ch).setMeta("_ts_registered", util.getTS());
                        temp.getChannels().get(ch).setMeta("_ts_last", util.getTS());
                        Generic.curProtocol.outPRVMSG(who, replyto, "" + Generic.Users.get(user).uid + ": Registration Succeeded!");
                        Logging.info("SRVCHAN", "Channel " + ch + " registered by " + user + " to " + u + ".");
                        Generic.curProtocol.srvJoin(who, ch, "+strn");
                        Generic.curProtocol.outSETMODE(who, ch, "+ro", who.getname());
                    } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Channel is already registered.");
                } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Owner to-be is not logged in!");
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: No such user is online!");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid Arguments. Usage: register [channel] [user]");
    }
