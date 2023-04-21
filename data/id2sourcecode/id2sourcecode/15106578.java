    public void informOpenChannels(String projectKey) {
        String[] clientIds = OpenSessionChannels.getClientIds(projectKey);
        if (clientIds == null) {
            return;
        }
        String exceptClientId = createClientId(projectKey);
        for (String clientId : clientIds) {
            if (!clientId.equals(exceptClientId)) {
                ChannelService channelService = ChannelServiceFactory.getChannelService();
                channelService.sendMessage(new ChannelMessage(clientId, "reloadModel"));
            }
        }
    }
