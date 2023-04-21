    @Override
    public String createToken(String clientId) {
        return ChannelServiceFactory.getChannelService().createChannel(clientId);
    }
