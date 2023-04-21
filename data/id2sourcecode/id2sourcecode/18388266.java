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
            if (((SrvHelp) who).getChannels().containsKey(whatchan)) {
                if (((SrvHelp) who).getChannels().get(whatchan).queue.contains(Generic.Users.get(whom))) {
                    Generic.curProtocol.outPRVMSG(who, whom, "Your help request has been abandoned.");
                    ((SrvHelp) who).getChannels().get(whatchan).queue.remove(Generic.Users.get(whom));
                }
            } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a registered channel!");
        } else if (!silent) Generic.curProtocol.outPRVMSG(who, replyto, "Error: Not a channel!");
    }
