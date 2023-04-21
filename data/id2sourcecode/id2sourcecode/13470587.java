    public void handle_command(Service who, String user, String replyto, String arguments) {
        for (Map.Entry<String, SrvChannel_channel> t : ((SrvChannel) who).getChannels().entrySet()) {
            Generic.curProtocol.outPRVMSG(who, replyto, "Entry " + t.toString());
        }
    }
