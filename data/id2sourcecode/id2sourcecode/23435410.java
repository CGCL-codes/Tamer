    public void handle_command(Service who, String user, String replyto, String arguments) {
        String whatchan = "";
        if (arguments.length() > 1) whatchan = arguments; else whatchan = replyto;
        whatchan = whatchan.toLowerCase();
        if (whatchan.startsWith("#")) {
            if (((SrvChannel) who).getChannels().containsKey(whatchan)) {
                Generic.curProtocol.outPRVMSG(who, user, "" + util.pad("USER", 12) + " " + "ACCESS");
                Generic.curProtocol.outPRVMSG(who, user, "------------------------------");
                for (Map.Entry<String, SrvChannel_channel.ChanAccess> t : ((SrvChannel) who).getChannels().get(whatchan).getUsers().entrySet()) {
                    Generic.curProtocol.outPRVMSG(who, user, "" + util.pad(t.getKey(), 12) + " " + t.getValue().toString().substring(2));
                }
            } else Generic.curProtocol.outPRVMSG(who, user, "Error: Not a registered channel!");
        } else Generic.curProtocol.outPRVMSG(who, user, "Error: Not a channel!");
    }
