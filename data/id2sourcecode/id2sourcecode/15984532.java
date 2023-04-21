    public static String getToken(final String clientId) {
        com.google.appengine.api.channel.ChannelService channelService = ChannelServiceFactory.getChannelService();
        String toReturn = channelService.createChannel(clientId);
        return toReturn;
    }
