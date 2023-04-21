    public void handle_command(Service who, String user, String replyto, String arguments) {
        SrvHelp temp = ((SrvHelp) who);
        String args[] = arguments.split(" ");
        if (args.length == 1) {
            String ch = args[0].toLowerCase();
            if (!temp.getChannels().containsKey(ch)) {
                temp.getChannels().put(ch, new SrvHelp_channel(ch));
                Generic.curProtocol.outPRVMSG(who, replyto, "" + Generic.Users.get(user).uid + ": Registration Succeeded!");
                Logging.info("SRVHELP", "Channel " + ch + " registered by " + user + ".");
                Generic.curProtocol.srvJoin(who, ch, "+stn");
                Generic.curProtocol.outSETMODE(who, ch, "+o", who.getname());
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Channel is already registered.");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid Arguments. Usage: register [channel]");
    }
