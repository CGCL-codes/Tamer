    public static String getToken(Long uid) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        return channelService.createChannel(String.valueOf(uid));
    }
