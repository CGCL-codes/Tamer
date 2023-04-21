    private void performJoinChannels(IrcServer ircServer) {
        if (ircServer == null) return;
        logger.debug("performing channels join ...");
        for (IrcChannel channel : ircServer.getChannels()) {
            logger.debug("joining channel: " + channel.getName());
            joinChannel(channel.getName());
        }
    }
