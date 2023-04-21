    private void log(final String message) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        for (String clientId : channelPresenceSevlet.getConnectedClientIds()) {
            channelService.sendMessage(new ChannelMessage(clientId, message));
        }
    }
