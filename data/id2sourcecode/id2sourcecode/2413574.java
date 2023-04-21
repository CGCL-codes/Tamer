    public PluginResult handle(Bot bot, InCommand command) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("enter handle(" + bot + ", " + command + ")");
        }
        boolean isPrivateToUs = false;
        if (command instanceof MessageCommand) {
            MessageCommand msg = (MessageCommand) command;
            isPrivateToUs = msg.isPrivateToUs(bot.getState());
        }
        ChatMessage chatMessage = ChatMessage.create(command, bot.getChannelName(), isPrivateToUs);
        if (chatMessage != null) {
            chatMessageHub.queueMessage(secret, chatMessage);
        }
        if (log.isDebugEnabled()) {
            log.debug("exit handle(" + bot + ", " + command + ")");
        }
        return PluginResult.NEXT;
    }
