    public void handle_command(Service who, String user, String replyto, String arguments) {
        SrvHelp temp = ((SrvHelp) who);
        String args[] = arguments.split(" ");
        String tU = args[0].toLowerCase();
        if (args.length == 1) {
            if (temp.getChannels().containsKey(tU)) {
                temp.getChannels().remove(tU);
                Generic.srvPart(who, tU, "Channel Unregistered.");
                Generic.curProtocol.outPRVMSG(who, replyto, "Channel removed.");
                Logging.info("SRVHELP", "Channel " + tU + " unregistered by " + user + ".");
            } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: No such channel is registered");
        } else Generic.curProtocol.outPRVMSG(who, replyto, "Error: Invalid Arguments. Usage: unregister <channel>");
    }
