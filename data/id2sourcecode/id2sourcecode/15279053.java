    public String addLoggedChannel(String channelName) {
        if (!started) {
            return "No channel configured for logging yet! The logging service is not started!";
        }
        if (StringUtils.isEmpty(logsDir)) {
            return "You must configure the logs directory first!";
        }
        Session session = botService.getSession();
        if (session == null) {
            return "Internal error no IRC session. Please make the bot join a channel first";
        }
        Channel channel = session.getChannel(channelName);
        if (channel == null) {
            return String.format("The bot must join the channel '%s', first!", channelName);
        }
        boolean channelAdded = IRCLogListener.addLoggedChannel(logsDir, channelName);
        if (channelAdded) {
            return String.format("The channel '%s' will be logged.", channelName);
        } else {
            return String.format("The bot might already be logging the channel '%s'", channelName);
        }
    }
