    @Override
    public String listenForModelUpdates(String contextKey, String projectName) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        String projectKey = createProjectKey(contextKey, projectName);
        String clientId = createClientId(projectKey);
        String channelToken = channelService.createChannel(clientId);
        DAO dao = ServerUtils.getDao();
        OpenSessionChannels openChannels = dao.find(OpenSessionChannels.class, projectKey);
        if (openChannels == null) {
            openChannels = new OpenSessionChannels(projectKey, clientId);
        } else {
            openChannels.addChannel(clientId);
        }
        dao.persistObject(openChannels);
        return channelToken;
    }
