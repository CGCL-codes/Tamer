    protected Channel getChannel(String channelName) {
        ChannelRepository repository = ChannelRepository.getInstance();
        return repository.getChannel(channelName);
    }
