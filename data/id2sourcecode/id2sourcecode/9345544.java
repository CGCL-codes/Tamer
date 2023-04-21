    private void sendMessage(ChatPartner cp, IMessage msg) throws IOException {
        if (!cp.getChannel().isOpen()) {
            Logger.logWarning("Channel to " + cp + " has been closed. Telling UI...");
            removeChatpartner(cp);
            return;
        }
        if (cp.getChannel().isConnected()) {
            commFacade.sendUDPMessage(cp.getChannel(), msg);
        } else {
            commFacade.sendUDPMessage(cp.getChannel(), msg, cp.getUsedAddress());
        }
    }
