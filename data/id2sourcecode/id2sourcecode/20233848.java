    protected void onJoin(String channel, String sender, String login, String hostname) {
        if (sender.equalsIgnoreCase(getNick())) return;
        for (UserJoinHandler joinHandler : joinHandlers) {
            if (joinHandler == null) continue;
            logger.debug("user join handler: " + joinHandler.getClass());
            IrcServer joinedServer = getJoinedServerDefinition();
            if (joinedServer == null) return;
            IrcChannel channelDefinition = joinedServer.getChannelByName(channel);
            if (channelDefinition == null) return;
            joinHandler.handlerUserJoin(this, channelDefinition, sender, login, hostname);
        }
    }
