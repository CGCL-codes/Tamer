    public static ChannelService getChannelService() {
        return new ChannelServiceImpl(GWT.<RemoteChannelServiceAsync>create(RemoteChannelService.class));
    }
